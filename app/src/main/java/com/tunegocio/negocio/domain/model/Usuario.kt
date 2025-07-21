
/**
 * Modelo de datos que representa a un usuario en la aplicación.
 *
 * @property id Identificador único del usuario.
 * @property nombreUsuario Nombre de usuario para login.
 * @property rol Rol o perfil del usuario (ejemplo: admin, cliente, etc.).
 * @property nombreCompleto Nombre completo del usuario.
 * @property telefono Número telefónico de contacto.
 *
 * Todos los campos tienen valores por defecto vacíos para facilitar
 * la creación de instancias sin argumentos.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */

package com.tunegocio.negocio.domain.model

data class Usuario(
    val id: String,
    val nombreUsuario: String,
    val contrasena: String, // hash
    val rol: String,
    val nombreCompleto: String,
    val telefono: String,
    val direccion: String,
    val estado: String,
    val fechaNacimiento: String,
    val fechaRegistro: String
)