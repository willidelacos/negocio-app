package com.tunegocio.negocio.domain.model.reserva

data class ReservaConProductos(
    val codigoTicket: String,
    val clienteId: String,
    val nombreCompleto: String,
    val nombreUsuario: String,
    val telefono: String,
    val fechaReserva: String,
    val fechaEntrega: String,
    val horaEntrega: String,
    val tipoPago: String,
    val estado: String,
    val observaciones: String,
    val puntosGanados: Int,
    val canjeado: String,
    val productos: List<ItemCarrito>
)
fun ReservaConProductos.toDetalleReservaBase() = DetalleReserva(
    id = "",
    clienteId = this.clienteId,
    nombreCompleto = this.nombreCompleto,
    nombreUsuario = this.nombreUsuario,
    telefono = this.telefono,
    codigoTicket = this.codigoTicket,
    fechaReserva = this.fechaReserva,
    fechaEntrega = this.fechaEntrega,
    horaEntrega = this.horaEntrega,
    estado = this.estado,
    tipoPago = this.tipoPago,
    total = 0.0,
    observaciones = this.observaciones,
    puntosGanados = this.puntosGanados,
    canjeado = this.canjeado,

    // ✅ nuevos campos requeridos
    productoId = "",
    nombreProducto = "",
    cantidad = 0,
    subtotal = 0.0
)
