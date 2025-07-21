package com.tunegocio.negocio.domain.model.reserva

data class DetalleReserva(
    val id: String,
    val clienteId: String,
    val nombreCompleto: String,
    val nombreUsuario: String,
    val telefono: String,
    val codigoTicket: String,
    val fechaReserva: String,
    val fechaEntrega: String,
    val horaEntrega: String,
    val estado: String,
    val tipoPago: String,
    val total: Double,
    val observaciones: String,
    val puntosGanados: Int,
    val canjeado: String,

    // ✅ Agrega estos si faltan:
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val subtotal: Double
)
