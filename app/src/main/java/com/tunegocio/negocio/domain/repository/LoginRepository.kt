/**
 * Interfaz que define el contrato para el repositorio de login.
 *
 * Implementa la función para autenticar a un usuario dado su nombre de usuario
 * y clave (ya hasheada).
 *
 * @return Usuario autenticado si las credenciales son correctas, null en caso contrario.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
package com.tunegocio.negocio.domain.repository

import com.tunegocio.negocio.domain.model.Usuario

interface LoginRepository {
    suspend fun login(nombreUsuario: String, clave: String): Usuario?
}