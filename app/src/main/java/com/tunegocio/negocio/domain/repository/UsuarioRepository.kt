package com.tunegocio.negocio.domain.repository

import com.tunegocio.negocio.domain.model.Usuario

interface UsuarioRepository {
    suspend fun listar(): List<Usuario>
    suspend fun agregar(usuario: Usuario): Boolean
    suspend fun actualizar(usuario: Usuario): Boolean
    suspend fun eliminar(id: String): Boolean
}