package com.tunegocio.negocio.domain.repository

import com.tunegocio.negocio.domain.model.reserva.DetalleReserva

interface DetalleReservaRepository {
    suspend fun listar(): List<DetalleReserva>
    suspend fun agregar(detalle: DetalleReserva): Boolean
    suspend fun actualizar(detalle: DetalleReserva): Boolean
    suspend fun eliminar(id: String): Boolean
}
