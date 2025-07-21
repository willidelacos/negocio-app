
package com.tunegocio.negocio.presentation.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunegocio.negocio.domain.usecase.producto.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tunegocio.negocio.domain.model.Producto

class ProductoViewModel(
    private val obtenerProductos: ObtenerProductosUseCase,
    private val agregarProducto: AgregarProductoUseCase,
    private val actualizarProducto: ActualizarProductoUseCase,
    private val eliminarProducto: EliminarProductoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductoState())
    val state: StateFlow<ProductoState> = _state



    fun onEvent(event: ProductoEvent) {
        when (event) {
            is ProductoEvent.CargarProductos -> cargar()
            is ProductoEvent.AgregarProducto -> agregar(event.producto)
            is ProductoEvent.ActualizarProducto -> actualizar(event.producto)
            is ProductoEvent.EliminarProducto -> eliminar(event.id)
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val productos = obtenerProductos()
            _state.value = _state.value.copy(productos = productos, isLoading = false)
        }
    }

    private fun agregar(producto: Producto) {
        viewModelScope.launch {
            if (agregarProducto(producto)) {
                onEvent(ProductoEvent.CargarProductos)
            }
        }
    }

    private fun actualizar(producto: Producto) {
        viewModelScope.launch {
            if (actualizarProducto(producto)) {
                onEvent(ProductoEvent.CargarProductos)
            }
        }
    }

    private fun eliminar(id: String) {
        viewModelScope.launch {
            if (eliminarProducto(id)) {
                onEvent(ProductoEvent.CargarProductos)
            }
        }
    }
}
