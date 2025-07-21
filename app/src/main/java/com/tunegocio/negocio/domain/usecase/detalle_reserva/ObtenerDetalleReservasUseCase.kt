package com.tunegocio.negocio.domain.usecase.detalle_reserva

import com.tunegocio.negocio.domain.repository.DetalleReservaRepository

class ObtenerDetalleReservasUseCase(private val repository: DetalleReservaRepository) {
    suspend operator fun invoke() = repository.listar()
}
