package org.metabrainz.android.presentation.features.userprofile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


//Colors
val Appbg = Color(0xFF263238)
val AppbgLight = Color(0xFFe0e0e0)
val EnabledChipColor = Color(0xFFccabff)
val DisabledChipColor = Color(0xFFd8cfe8)
val ChipTextColor = Color(0xFF323232)

private val DarkColorPalette = darkColors(
    surface = Appbg
)

private val LightColorPalette = lightColors(
    surface = AppbgLight
)

@Composable
fun ProfileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}