package com.tunegocio.negocio.presentation.usuarios

import com.tunegocio.negocio.domain.model.Usuario

data class UsuarioState(
    val usuarios: List<Usuario> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null
)