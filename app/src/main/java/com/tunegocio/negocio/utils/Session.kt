package com.tunegocio.negocio.utils

import com.tunegocio.negocio.domain.model.Usuario

object Session {
    var usuarioActual: Usuario? = null

    fun iniciarSesion(usuario: Usuario) {
        usuarioActual = usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun estaLogueado(): Boolean {
        return usuarioActual != null
    }

    fun obtenerRol(): String? {
        return usuarioActual?.rol
    }

    fun obtenerNombreCompleto(): String? {
        return usuarioActual?.nombreCompleto
    }
}