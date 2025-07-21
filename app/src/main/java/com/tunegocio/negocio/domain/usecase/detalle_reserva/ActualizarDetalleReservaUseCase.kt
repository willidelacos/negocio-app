package com.tunegocio.negocio.domain.usecase.detalle_reserva

import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
import com.tunegocio.negocio.domain.repository.DetalleReservaRepository

class ActualizarDetalleReservaUseCase(private val repository: DetalleReservaRepository) {
    suspend operator fun invoke(reserva: DetalleReserva): Boolean {
        return repository.actualizar(reserva)
    }
}
