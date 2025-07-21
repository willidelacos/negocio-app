package com.tunegocio.negocio.domain.usecase.detalle_reserva

import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
import com.tunegocio.negocio.domain.repository.DetalleReservaRepository

class AgregarDetalleReservaUseCase(private val repository: DetalleReservaRepository) {
    suspend operator fun invoke(detalle: DetalleReserva) = repository.agregar(detalle)
}
