

/**
 * ViewModel responsable del proceso de autenticación (login).
 *
 * - Recibe eventos desde la UI para iniciar el login.
 * - Usa LoginUseCase para validar usuario y contraseña.
 * - Guarda sesión usando UserSessionManager si login es exitoso.
 * - Actualiza estado observable (LoginState) para notificar UI.
 *
 * IMPORTANTE: Este módulo controla la seguridad y sesión del usuario,
 * cualquier cambio debe ser analizado rigurosamente.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
package com.tunegocio.negocio.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunegocio.negocio.domain.usecase.LoginUseCase
import com.tunegocio.negocio.utils.SecurityUtils
import com.tunegocio.negocio.utils.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tunegocio.negocio.data.local.UserSessionManager

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userSessionManager: UserSessionManager
) : ViewModel() {
    // Estado interno observable por la UI
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state
    /**
     * Procesa eventos de la UI.
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Submit -> login(event.nombreUsuario, event.clave)
        }
    }
    /**
     * Realiza la lógica de login: hash de contraseña, llamada a UseCase,
     * guarda la sesión si el usuario es válido y actualiza el estado.
     */
    private fun login(nombreUsuario: String, clave: String) {
        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)
            val hash = SecurityUtils.hashPassword(clave)
            val usuario = loginUseCase(nombreUsuario, hash)

            _state.value = if (usuario != null) {
                // ✅ Guardar la sesión en DataStore
                userSessionManager.guardarSesion(usuario)


                LoginState(usuario = usuario)
            } else {
                LoginState(error = "Usuario o clave incorrecta")
            }
        }
    }
}
