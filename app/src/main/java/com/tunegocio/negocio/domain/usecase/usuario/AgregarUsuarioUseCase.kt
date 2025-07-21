package com.tunegocio.negocio.domain.usecase.usuario

import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.repository.UsuarioRepository


class AgregarUsuarioUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(usuario: Usuario): Boolean {
        return repository.agregar(usuario)
    }
}