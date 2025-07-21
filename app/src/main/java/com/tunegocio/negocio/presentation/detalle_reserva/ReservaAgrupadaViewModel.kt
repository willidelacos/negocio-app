package com.tunegocio.negocio.presentation.detalle_reserva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunegocio.negocio.data.remote.detalle_reserva.DetalleReservaRemoteDataSource
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.time.LocalDate

class ReservaAgrupadaViewModel(
    private val clienteId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ReservaAgrupadaState())
    val state: StateFlow<ReservaAgrupadaState> = _state

    fun cargarPorFecha(fecha: String) {
        Log.d("ReservaVM", "🔹 cargarPorFecha() → clienteId=$clienteId, fecha=$fecha")

        viewModelScope.launch {
            _state.value = ReservaAgrupadaState(isLoading = true)
            try {
                val data = DetalleReservaRemoteDataSource.listarReservasPorClienteYFecha(clienteId, fecha)
                Log.d("ReservaVM", "✅ Datos cargados: ${data.size} reservas")
                _state.value = ReservaAgrupadaState(reservas = data)
            } catch (e: Exception) {
                Log.e("ReservaVM", "❌ Error: ${e.message}")
                _state.value = ReservaAgrupadaState(error = e.message)
            }
        }
    }

    fun cargarPorRango(inicio: String, fin: String) {
        viewModelScope.launch {
            _state.value = ReservaAgrupadaState(isLoading = true)
            try {
                val data = DetalleReservaRemoteDataSource.listarReservasPorClienteYRango(clienteId, inicio, fin)
                _state.value = ReservaAgrupadaState(reservas = data)
            } catch (e: Exception) {
                _state.value = ReservaAgrupadaState(error = e.message)
            }
        }
    }

    fun actualizarReservaLote(reserva: ReservaConProductos, camposActualizados: DetalleReserva) {
        viewModelScope.launch {
            val detallesActualizados = reserva.productos.map { item ->
                DetalleReserva(
                    id = item.id,
                    clienteId = reserva.clienteId,
                    nombreCompleto = reserva.nombreCompleto,
                    nombreUsuario = reserva.nombreUsuario,
                    telefono = reserva.telefono,
                    codigoTicket = reserva.codigoTicket,
                    fechaReserva = reserva.fechaReserva,
                    fechaEntrega = camposActualizados.fechaEntrega,
                    horaEntrega = camposActualizados.horaEntrega,
                    estado = camposActualizados.estado,
                    tipoPago = camposActualizados.tipoPago,
                    total = item.subtotal,
                    observaciones = camposActualizados.observaciones,
                    puntosGanados = camposActualizados.puntosGanados,
                    canjeado = camposActualizados.canjeado,

                    // ✅ CAMPOS NUEVOS OBLIGATORIOS
                    productoId = item.productoId,
                    nombreProducto = item.nombreProducto,
                    cantidad = item.cantidad,
                    subtotal = item.subtotal
                )
            }


            val exito = DetalleReservaRemoteDataSource.actualizarReservaMultiple(detallesActualizados)
            if (exito) {
                cargarPorFecha(obtenerFechaHoy()) // recarga la lista del día
            }
        }
    }

    private fun obtenerFechaHoy(): String = LocalDate.now().toString() // yyyy-MM-dd
}
