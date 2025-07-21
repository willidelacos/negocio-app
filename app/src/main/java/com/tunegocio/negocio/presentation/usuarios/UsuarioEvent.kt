package com.tunegocio.negocio.presentation.usuarios

import com.tunegocio.negocio.domain.model.Usuario

sealed class UsuarioEvent {
    object LoadUsuarios : UsuarioEvent()
    data class Agregar(val usuario: Usuario) : UsuarioEvent()
    data class Actualizar(val usuario: Usuario) : UsuarioEvent()
    data class Eliminar(val id: String) : UsuarioEvent()
}