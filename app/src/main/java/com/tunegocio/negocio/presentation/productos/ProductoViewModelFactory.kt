package com.tunegocio.negocio.presentation.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunegocio.negocio.domain.usecase.producto.ProductoUseCases

class ProductoViewModelFactory(
    private val useCases: ProductoUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(
                obtenerProductos = useCases.obtenerProductos,
                agregarProducto = useCases.agregarProducto,
                actualizarProducto = useCases.actualizarProducto,
                eliminarProducto = useCases.eliminarProducto
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
