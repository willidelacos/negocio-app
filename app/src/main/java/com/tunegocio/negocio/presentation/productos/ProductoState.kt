package com.tunegocio.negocio.presentation.productos

import com.tunegocio.negocio.domain.model.Producto

data class ProductoState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
