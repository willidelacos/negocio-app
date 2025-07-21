package com.tunegocio.negocio.domain.usecase

import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.repository.LoginRepository

/**
 * Caso de uso para realizar el proceso de login.
 *
 * Esta clase delega la autenticación al repositorio de login,
 * recibiendo nombre de usuario y clave (hashed),
 * y devuelve el usuario autenticado o null si no es válido.
 *
 * Permite centralizar la lógica de login para mantener la arquitectura limpia.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(nombreUsuario: String, clave: String): Usuario? {
        return repository.login(nombreUsuario, clave)
    }
}