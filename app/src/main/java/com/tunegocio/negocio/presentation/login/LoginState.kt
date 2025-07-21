
/**
 * Estado de la pantalla de Login.
 *
 * Contiene:
 * - isLoading: indica si la operación de login está en progreso.
 * - usuario: el usuario autenticado, si el login fue exitoso.
 * - error: mensaje de error si el login falló.
 *
 * Esta clase es clave para el flujo de autenticación,
 * se recomienda no modificar su estructura sin un análisis previo.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
package com.tunegocio.negocio.presentation.login
import com.tunegocio.negocio.domain.model.Usuario

data class LoginState(
    val isLoading: Boolean = false,
    val usuario: Usuario? = null,
    val error: String? = null
)