package com.tunegocio.negocio.domain.model

data class Producto(
    val id: String,
    val nombreProducto: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String,
    val activo: Boolean,
    val recompezas: Int
)