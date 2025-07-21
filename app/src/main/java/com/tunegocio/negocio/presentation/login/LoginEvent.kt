package com.tunegocio.negocio.presentation.login

sealed class LoginEvent {
    data class Submit(val nombreUsuario: String, val clave: String) : LoginEvent()
}