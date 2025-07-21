package com.tunegocio.negocio.domain.usecase.detalle_reserva

import com.tunegocio.negocio.domain.repository.DetalleReservaRepository

class EliminarDetalleReservaUseCase(private val repository: DetalleReservaRepository) {
    suspend operator fun invoke(id: String) = repository.eliminar(id)
}
