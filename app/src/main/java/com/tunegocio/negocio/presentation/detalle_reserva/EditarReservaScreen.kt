package com.tunegocio.negocio.presentation.detalle_reserva

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tunegocio.negocio.domain.model.reserva.ItemCarrito
import com.tunegocio.negocio.domain.model.Producto
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import com.tunegocio.negocio.data.remote.producto.ProductoRemoteDataSource
import kotlinx.coroutines.launch

@Composable
fun EditarReservaScreen(
    navController: NavController,
    reserva: ReservaConProductos,
    viewModel: EditarReservaViewModel = remember { EditarReservaViewModel(reserva) }
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Carrito observable
    val carrito = viewModel.carrito.collectAsState().value

    // Cargar productos disponibles (una sola vez)
    val productosDisponibles by produceState(initialValue = emptyList<Producto>()) {
        value = viewModel.obtenerProductosDisponibles()
    }

    CarritoScreen(
        productosDisponibles = productosDisponibles,
        carrito = carrito,
        onCantidadChange = { viewModel.actualizarItem(it) },
        onEliminarItem = { viewModel.eliminarItem(it) },
        onConfirmar = {
            scope.launch {
                val ok = viewModel.confirmarEdicion(it)
                if (ok) {
                    navController.popBackStack()
                } else {
                    // Puedes usar Snackbar o Toast aquí si deseas
                }
            }
        },
        onCancelar = { navController.popBackStack() }
    )
}
