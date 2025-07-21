package com.tunegocio.negocio.presentation.detalle_reserva

import androidx.lifecycle.ViewModel
import com.tunegocio.negocio.domain.model.reserva.ItemCarrito
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import com.tunegocio.negocio.data.remote.detalle_reserva.DetalleReservaRemoteDataSource
import com.tunegocio.negocio.data.remote.producto.ProductoRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tunegocio.negocio.domain.model.reserva.toDetalleReservaBase


class EditarReservaViewModel(
    private val reserva: ReservaConProductos
) : ViewModel() {

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(reserva.productos)
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    fun actualizarItem(item: ItemCarrito) {
        _carrito.update { lista ->
            lista.map { if (it.productoId == item.productoId) item else it }
        }
    }

    fun eliminarItem(item: ItemCarrito) {
        _carrito.update { lista ->
            lista.filterNot { it.productoId == item.productoId }
        }
    }

    suspend fun obtenerProductosDisponibles() = withContext(Dispatchers.IO) {
        ProductoRemoteDataSource.listarProductos().filter { it.activo && it.stock > 0 }
    }

    suspend fun confirmarEdicion(nuevoCarrito: List<ItemCarrito>): Boolean {
        val nuevosDetalles = nuevoCarrito.map { item: ItemCarrito ->
            reserva.toDetalleReservaBase().copy(
                id = item.id,
                productoId = item.productoId,
                nombreProducto = item.nombreProducto,
                cantidad = item.cantidad,
                subtotal = item.subtotal,
                total = item.subtotal
            )
        }

        return DetalleReservaRemoteDataSource.actualizarReservaMultiple(nuevosDetalles)
    }


}
