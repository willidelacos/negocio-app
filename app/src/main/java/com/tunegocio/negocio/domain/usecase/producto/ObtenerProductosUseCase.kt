package com.tunegocio.negocio.domain.usecase.producto

import com.tunegocio.negocio.domain.repository.ProductoRepository

class ObtenerProductosUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke() = repository.listar()
}