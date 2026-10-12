package com.device.deviceos.core.network

import com.device.deviceos.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * 后端约定的成功状态码
 */
const val CODE_SUCCESS = 200

/**
 * 网络请求客户端（单例）
 *
 * 职责：
 * 1. 创建并持有全局唯一的 Retrofit / OkHttpClient
 * 2. 统一拼接请求头、超时时间
 * 3. 自动携带登录后拿到的 token
 * 4. 配合 safeApiCall，把 ApiResponse 和异常统一转换成 NetworkResult
 *
 * 用法：
 *
 *     // 1. 定义接口（放在 core/api 下）
 *     interface UserApi {
 *         @POST("user/login")
 *         suspend fun login(@Body body: LoginRequest): ApiResponse<LoginData>
 *     }
 *
 *     // 2. 拿到接口实现
 *     private val api = RetrofitClient.create<UserApi>()
 *
 *     // 3. 在 ViewModel 里调用
 *     viewModelScope.launch {
 *         when (val result = safeApiCall { api.login(body) }) {
 *             is NetworkResult.Loading -> ...
 *             is NetworkResult.Success -> ...
 *             is NetworkResult.Error   -> ...
 *         }
 *     }
 */
object RetrofitClient {
    /**
     * 后端地址
     */
    private const val BASE_URL = "https://10.0.2.2:8080/api/"

    /**
     * 超时时间（秒）
     */
    private const val TIMEOUT = 15L

    /**
     * 登录成功后保存的 token
     *
     * 目前只存在内存里，App 被杀掉就没了。
     * 需要长期保存时，登录成功后写进 SharedPreferences / DataStore，
     * App 启动时再调用 saveToken 恢复即可。
     */
    @Volatile
    private var token: String? = null

    /** 保存 token，登录成功后调用 */
    fun saveToken(value: String?) {
        token = value
    }

    /** 清除 token，退出登录时调用 */
    fun clearToken() {
        token = null
    }

    /** 读取当前 token */
    fun currentToken(): String? = token

    /**
     * Gson 实例，Retrofit 与业务代码共用同一份配置
     */
    private val gson: Gson by lazy {
        GsonBuilder()
            // 后端字段为 null 时也能正常解析
            .serializeNulls()
            .create()
    }

    /**
     * 网络日志
     *
     * 开发环境查看请求参数，正式包关掉，避免泄露账号密码
     */
    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * 请求头拦截器：统一 Content-Type，并自动带上 token
     */
    private val headerInterceptor: Interceptor by lazy {
        Interceptor { chain ->
            val builder = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")

            token?.takeIf { it.isNotBlank() }?.let {
                builder.addHeader("Authorization", "Bearer $it")
            }

            chain.proceed(builder.build())
        }
    }

    /**
     * 全局唯一的 OkHttpClient
     *
     * OkHttp 的连接池、线程池都挂在实例上，
     * 每次请求都 new 一个会导致连接无法复用，所以必须共用一份。
     */
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * 全局唯一的 Retrofit
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    /**
     * 创建接口实现类
     *
     * @param clazz 接口的 Class 对象
     */
    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)

    /**
     * 创建接口实现类，支持泛型写法
     *
     * 例如：RetrofitClient.create<UserApi>()
     */
    inline fun <reified T> create(): T = create(T::class.java)
}

/**
 * 统一发起请求并处理结果
 *
 * 把「后端返回错误码」和「请求过程抛异常」这两种失败情况，
 * 统一收敛成 NetworkResult.Error，页面层只需要 when 三个分支。
 *
 * @param block 真正的请求，例如 { api.login(body) }
 */
suspend fun <T> safeApiCall(
    block: suspend () -> ApiResponse<T>
): NetworkResult<T> = withContext(Dispatchers.IO) {
    try {
        val response = block()

        if (response.code == CODE_SUCCESS) {
            NetworkResult.Success(response.data)
        } else {
            // 业务层面的失败，例如密码错误、手机号已注册
            NetworkResult.Error(
                response.msg.ifBlank { "请求失败（${response.code}）" }
            )
        }
    } catch (e: CancellationException) {
        // 协程被取消不是错误，必须原样抛出，否则会吞掉取消信号
        throw e
    } catch (e: SocketTimeoutException) {
        NetworkResult.Error("连接超时，请检查网络后重试")
    } catch (e: UnknownHostException) {
        NetworkResult.Error("无法连接服务器，请检查网络")
    } catch (e: HttpException) {
        NetworkResult.Error("服务器异常（${e.code()}）")
    } catch (e: IOException) {
        NetworkResult.Error("网络异常，请稍后重试")
    } catch (e: Exception) {
        NetworkResult.Error(e.message ?: "未知错误")
    }
}
