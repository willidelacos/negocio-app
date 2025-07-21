package com.tunegocio.negocio.domain.usecase.producto

import com.tunegocio.negocio.domain.model.Producto
import com.tunegocio.negocio.domain.repository.ProductoRepository

class ActualizarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(producto: Producto): Boolean {
        return repository.actualizar(producto)
    }
}