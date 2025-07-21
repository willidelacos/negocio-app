// ItemCarrito.kt
package com.tunegocio.negocio.domain.model.reserva

data class ItemCarrito(
    val id: String = "", // ← este campo debe venir desde el backend (Sheets) // ← necesario para identificar la fila original
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val stockDisponible: Int // ← nuevo campo
) {
    val subtotal: Double get() = cantidad * precioUnitario
}
