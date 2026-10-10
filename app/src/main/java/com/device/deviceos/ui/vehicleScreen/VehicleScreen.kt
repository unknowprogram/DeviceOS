package com.device.deviceos.ui.vehicleScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VehicleScreen(
    onBackClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Vehicle Screen", modifier = Modifier.fillMaxSize())
    }
}
