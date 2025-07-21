package com.tunegocio.negocio.domain.usecase.usuario

import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.repository.UsuarioRepository

class ActualizarUsuarioUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario) = repository.actualizar(usuario)
}