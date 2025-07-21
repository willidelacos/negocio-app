package com.tunegocio.negocio.domain.usecase.producto

import com.tunegocio.negocio.domain.repository.ProductoRepository

class EliminarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(id: String): Boolean {
        return repository.eliminar(id)
    }
}