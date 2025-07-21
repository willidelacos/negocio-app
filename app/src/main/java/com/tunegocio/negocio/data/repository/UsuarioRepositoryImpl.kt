package com.tunegocio.negocio.data.repository

//import com.tunegocio.negocio.data.remote.UsuarioRemoteDataSource
import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.repository.UsuarioRepository
import com.tunegocio.negocio.data.remote.usuario.UsuarioRemoteDataSource

import android.util.Log

class UsuarioRepositoryImpl : UsuarioRepository {
    override suspend fun listar(): List<Usuario> {
        return UsuarioRemoteDataSource.listarUsuarios()
    }

    override suspend fun agregar(usuario: Usuario): Boolean {
        Log.d("UsuarioRepositoryImpl", "→ Llamando agregarUsuario")
        return UsuarioRemoteDataSource.agregarUsuario(usuario)
    }

    override suspend fun actualizar(usuario: Usuario): Boolean {
        return UsuarioRemoteDataSource.actualizarUsuario(usuario)
    }

    override suspend fun eliminar(id: String): Boolean {
        return UsuarioRemoteDataSource.eliminarUsuario(id)
    }
}
