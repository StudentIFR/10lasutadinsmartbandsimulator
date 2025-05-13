package com.example.smartbandsimulator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary      = Color(0xFF6200EE),
    onPrimary    = Color.White,
    secondary    = Color(0xFF03DAC6),
    onSecondary  = Color.Black,
    background   = Color(0xFFF2F2F2),
    onBackground = Color.Black,
    surface      = Color.White,
    onSurface    = Color.Black,
    error        = Color(0xFFB00020),
    onError      = Color.White,
)

@Composable
fun SmartBandSimulatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = MaterialTheme.typography,
        content     = content
    )
}
