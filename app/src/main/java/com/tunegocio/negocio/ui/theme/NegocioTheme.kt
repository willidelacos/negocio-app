package com.tunegocio.negocio.ui.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
// Definición de esquema de colores para modo claro
private val LightColors = lightColorScheme(
    primary = Color(0xFF8BC34A),    // Verde claro para color primario
    secondary = Color(0xFFFFC107),  // Amarillo para color secundario
    tertiary = Color(0xFF03A9F4)    // Azul para color terciario
)

// Definición de esquema de colores para modo oscuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CAF50),    // Verde oscuro para color primario
    secondary = Color(0xFFFF9800),  // Naranja para color secundario
    tertiary = Color(0xFF00BCD4)    // Azul cian para color terciario
)
/**
 * Composable que aplica el tema global de la app
 *
 * @param darkTheme Determina si se usa modo oscuro (por defecto detecta sistema)
 * @param content Composable que representa el contenido UI que será estilizado
 */
@Composable
fun NegocioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),// Puedes personalizar tipografías aquí
        content = content
    )
}
