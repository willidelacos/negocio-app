package com.tunegocio.negocio.presentation.detalle_reserva

import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos

data class ReservaAgrupadaState(
    val reservas: List<ReservaConProductos> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)