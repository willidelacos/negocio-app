package com.tunegocio.negocio.domain.usecase.usuario

import com.tunegocio.negocio.domain.repository.UsuarioRepository

class EliminarUsuarioUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(id: String) = repository.eliminar(id)
}