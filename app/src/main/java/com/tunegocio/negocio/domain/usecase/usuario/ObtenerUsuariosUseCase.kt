package com.tunegocio.negocio.domain.usecase.usuario

import com.tunegocio.negocio.domain.repository.UsuarioRepository

class ObtenerUsuariosUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke() = repository.listar()
}