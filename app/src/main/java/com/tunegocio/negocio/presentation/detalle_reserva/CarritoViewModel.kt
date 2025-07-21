package com.tunegocio.negocio.presentation.detalle_reserva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.model.reserva.ItemCarrito
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
import com.tunegocio.negocio.data.remote.detalle_reserva.DetalleReservaRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


data class CarritoState(
    val carrito: List<ItemCarrito> = emptyList(),
    val reservaActual: ReservaConProductos? = null,
    val exito: Boolean = false,
    val error: String? = null
)

class CarritoViewModel : ViewModel() {

    private val _state = MutableStateFlow(CarritoState())
    val state: StateFlow<CarritoState> = _state

    fun actualizarItem(item: ItemCarrito) {
        _state.update { current ->
            val nuevoCarrito = current.carrito.toMutableList()
            val index = nuevoCarrito.indexOfFirst { it.productoId == item.productoId }
            if (index != -1) {
                nuevoCarrito[index] = item
            } else {
                nuevoCarrito.add(item)
            }
            current.copy(carrito = nuevoCarrito)
        }
    }

    fun cargarCarrito(carrito: List<ItemCarrito>) {
        _state.update { it.copy(carrito = carrito) }
    }

    fun eliminarItem(item: ItemCarrito) {
        _state.update {
            it.copy(carrito = it.carrito.filterNot { c -> c.productoId == item.productoId })
        }
    }

    fun confirmarPedido(usuario: Usuario) {
        viewModelScope.launch {
            try {
                val ok = DetalleReservaRemoteDataSource.registrarCarrito(
                    carrito = _state.value.carrito,
                    clienteId = usuario.id,
                    nombreCompleto = usuario.nombreCompleto,
                    nombreUsuario = usuario.nombreUsuario,
                    telefono = usuario.telefono,
                    fechaReserva = obtenerFechaHoy(),
                    estado = "Pendiente"
                )
                _state.update { it.copy(exito = ok) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "❌ Error al confirmar: ${e.message}") }
            }
        }
    }

    fun cargarReservaParaEdicion(reserva: ReservaConProductos) {
        _state.update {
            it.copy(
                carrito = reserva.productos,
                reservaActual = reserva
            )
        }
    }

    fun actualizarReservaLote(
        reservaOriginal: ReservaConProductos,
        camposActualizados: DetalleReserva
    ) {
        viewModelScope.launch {
            val detallesActualizados = reservaOriginal.productos.map { item ->
                DetalleReserva(
                    id = item.id,
                    clienteId = reservaOriginal.clienteId,
                    nombreCompleto = reservaOriginal.nombreCompleto,
                    nombreUsuario = reservaOriginal.nombreUsuario,
                    telefono = reservaOriginal.telefono,
                    codigoTicket = reservaOriginal.codigoTicket,
                    fechaReserva = reservaOriginal.fechaReserva,
                    fechaEntrega = camposActualizados.fechaEntrega,
                    horaEntrega = camposActualizados.horaEntrega,
                    estado = camposActualizados.estado,
                    tipoPago = camposActualizados.tipoPago,
                    total = item.subtotal,
                    observaciones = camposActualizados.observaciones,
                    puntosGanados = camposActualizados.puntosGanados,
                    canjeado = camposActualizados.canjeado,

                    // ✅ Añadir estos campos:
                    productoId = item.productoId,
                    nombreProducto = item.nombreProducto,
                    cantidad = item.cantidad,
                    subtotal = item.subtotal
                )
            }


            val exito = DetalleReservaRemoteDataSource.actualizarReservaMultiple(detallesActualizados)
            if (exito) {
                _state.update { it.copy(exito = true) }
            } else {
                _state.update { it.copy(error = "❌ Error al actualizar reserva") }
            }
        }
    }

    private fun obtenerFechaHoy(): String {
        return LocalDate.now().toString()
    }
}
