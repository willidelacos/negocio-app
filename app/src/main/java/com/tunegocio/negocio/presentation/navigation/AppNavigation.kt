
package com.tunegocio.negocio.presentation.navigation

// AndroidX - Compose
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

// AndroidX - Navigation
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Sistema
import android.util.Log

// Proyecto - Utils y sesión
import com.tunegocio.negocio.data.local.UserSessionManager
import com.tunegocio.negocio.utils.Session

// Proyecto - Pantallas generales
import com.tunegocio.negocio.presentation.login.LoginScreen
import com.tunegocio.negocio.presentation.home.HomeScreen

// Proyecto - Usuarios
import com.tunegocio.negocio.data.repository.UsuarioRepositoryImpl
import com.tunegocio.negocio.domain.usecase.UsuarioUseCases
import com.tunegocio.negocio.presentation.usuarios.UsuarioFormScreen
import com.tunegocio.negocio.presentation.usuarios.UsuarioScreen
import com.tunegocio.negocio.presentation.usuarios.UsuarioViewModel
import com.tunegocio.negocio.presentation.usuarios.UsuarioViewModelFactory

// Proyecto - Productos
import com.tunegocio.negocio.data.repository.ProductoRepositoryImpl
import com.tunegocio.negocio.domain.usecase.producto.ProductoUseCases
import com.tunegocio.negocio.domain.usecase.producto.AgregarProductoUseCase
import com.tunegocio.negocio.domain.usecase.producto.ActualizarProductoUseCase
import com.tunegocio.negocio.domain.usecase.producto.EliminarProductoUseCase
import com.tunegocio.negocio.domain.usecase.producto.ObtenerProductosUseCase
import com.tunegocio.negocio.presentation.productos.ProductoEvent
import com.tunegocio.negocio.presentation.productos.ProductoFormScreen
import com.tunegocio.negocio.presentation.productos.ProductoScreen
import com.tunegocio.negocio.presentation.productos.ProductoViewModel
import com.tunegocio.negocio.presentation.productos.ProductoViewModelFactory
import androidx.compose.ui.platform.LocalContext
import com.tunegocio.negocio.presentation.detalle_reserva.*
import com.tunegocio.negocio.domain.model.Producto
import kotlinx.coroutines.launch
import com.tunegocio.negocio.data.remote.producto.ProductoRemoteDataSource
import com.tunegocio.negocio.utils.abrirWhatsApp
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva

@Composable
fun AppNavigation(userSessionManager: UserSessionManager) {
    val navController = rememberNavController()
    var inicializo by remember { mutableStateOf(false) }

    // Crear instancia del repositorio
    val usuarioRepository = remember { UsuarioRepositoryImpl() }
    // Crear casos de uso
    val usuarioUseCases = remember { UsuarioUseCases.provide(usuarioRepository) }
    // Crear factory para ViewModel
    val usuarioViewModelFactory = remember { UsuarioViewModelFactory(usuarioUseCases) }


    val productoRepository = remember { ProductoRepositoryImpl() }
    val productoUseCases = remember { ProductoUseCases.provide(productoRepository) }
    val productoViewModelFactory = remember { ProductoViewModelFactory(productoUseCases) }

    val productoViewModel: ProductoViewModel = viewModel(factory = productoViewModelFactory)


    // Al iniciar, verificamos si hay sesión guardada en DataStore
    LaunchedEffect(Unit) {
        val usuarioGuardado = userSessionManager.obtenerUsuarioSesion()
        if (usuarioGuardado != null) {
            // Si existe sesión, la cargamos en memoria y navegamos a Home
            Session.iniciarSesion(usuarioGuardado)
            navController.navigate("home") {
                // Limpia la pila para que no vuelva al login con back
                popUpTo("login") { inclusive = true }
            }
        } else {
            // Si no hay sesión, aseguramos navegación a Login
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
        inicializo = true
    }
// Definimos el host de navegación con destinos
    NavHost(
        navController = navController,
        startDestination = if (inicializo) "login" else "login"
    ) {
        composable("login") {
            LoginScreen(navController, userSessionManager)
        }
        composable("home") {
            HomeScreen(navController, userSessionManager)
        }
        composable("usuarios") {
            // Aquí creas el ViewModel con el factory
            val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)
            UsuarioScreen(navController, usuarioViewModel)
        }
        //para formulario de usuario registro y actualizacion
        composable("usuarioForm") {
            val viewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)
            UsuarioFormScreen(navController, viewModel)
        }

        composable("usuarioForm/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val viewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)

            val state by viewModel.state.collectAsState()
            val usuario = state.usuarios.find { it.id == id }

            // 🔁 Espera a que los usuarios estén cargados
            if (usuario != null) {
                Log.d("Nav", "Usuario encontrado con ID: $id")
                UsuarioFormScreen(navController, viewModel, usuario)
            } else {
                // Muestra un loader hasta que cargue
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }



        composable("productos") {
            ProductoScreen(navController, productoViewModel)
        }



        composable("producto_form") {
            ProductoFormScreen(
                navController = navController,
                viewModel = productoViewModel
            )
        }

        composable("producto_form?id={id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val state by productoViewModel.state.collectAsState()

            // ⚠️ Lanzar carga si productos vacíos
            LaunchedEffect(state.productos.isEmpty()) {
                if (state.productos.isEmpty()) {
                    productoViewModel.onEvent(ProductoEvent.CargarProductos)
                }
            }

            val producto = state.productos.find { it.id == id }

            if (producto != null) {
                ProductoFormScreen(
                    navController = navController,
                    viewModel = productoViewModel,
                    producto = producto
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }


        composable("detalle_reservas_agrupado") {
            val usuario = Session.usuarioActual
            val clienteId = usuario?.id ?: ""

            val viewModel = remember {
                ReservaAgrupadaViewModel(clienteId)
            }

            val context = LocalContext.current // ✅ este es el verdadero Context

            DetalleReservaScreenAgrupada(
                viewModel = viewModel,
                navController = navController,
                onEditar = { reserva ->
                    navController.navigate("detalle_reserva_form?id=${reserva.codigoTicket}")
                },
                onEliminar = { /* TODO */ },
                onWhatsApp = { reserva ->
                    abrirWhatsApp(
                        context = context,
                        reserva = reserva,
                        fechaEntrega = reserva.fechaReserva,
                        horaEntrega = "09:00" // o reserva.horaEntrega si lo tenés
                    )
                },
                onReservar = { /* TODO */ },
                onRecoger = { /* TODO */ }
            )
        }


        composable("detalle_reserva_form?id={id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val viewModel = remember { ReservaAgrupadaViewModel(Session.usuarioActual?.id ?: "") }
            val state by viewModel.state.collectAsState()

            val reservaAgrupada = state.reservas.find { it.codigoTicket == id }

            // Convertimos un producto de la reserva en DetalleReserva (solo para edición de un campo común)
            val primerDetalle = reservaAgrupada?.productos?.firstOrNull()?.let { item ->
                DetalleReserva(
                    id = "",
                    clienteId = "",
                    nombreCompleto = "",
                    nombreUsuario = "",
                    telefono = "",
                    codigoTicket = "",
                    fechaReserva = "",
                    fechaEntrega = "",
                    horaEntrega = "",
                    estado = "",
                    tipoPago = "",
                    total = 0.0,
                    observaciones = "",
                    puntosGanados = 0,
                    canjeado = "",

                    productoId = "",
                    nombreProducto = "",
                    cantidad = 0,
                    subtotal = 0.0
                )

            }

            DetalleReservaFormScreen(
                reserva = primerDetalle,
                onGuardar = { camposActualizados ->
                    reservaAgrupada?.let {
                        viewModel.actualizarReservaLote(
                            reserva = it,
                            camposActualizados = camposActualizados
                        )
                    }
                },
                onVolver = { navController.popBackStack() }
            )






            composable("carrito") {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                // Productos cargados desde Google Sheets
                var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }

                // ViewModel del carrito
                val carritoViewModel = remember { CarritoViewModel() }
                val carritoState by carritoViewModel.state.collectAsState()

                val usuario = Session.usuarioActual!!

                // Cargar productos una vez
                LaunchedEffect(Unit) {
                    scope.launch {
                        productos = ProductoRemoteDataSource.listarProductos()
                        Log.d("Carrito", "Productos cargados: ${productos.size}")
                    }
                }

                // Mostrar pantalla del carrito
                CarritoScreen(
                    productosDisponibles = productos,
                    carrito = carritoState.carrito,
                    onCantidadChange = { carritoViewModel.actualizarItem(it) },
                    onEliminarItem = { carritoViewModel.eliminarItem(it) },
                    onConfirmar = {
                        carritoViewModel.cargarCarrito(it)
                        carritoViewModel.confirmarPedido(usuario)
                    },
                    onCancelar = { navController.popBackStack() }
                )

                // Si se confirmó el pedido, cerrar pantalla
                LaunchedEffect(carritoState.exito) {
                    if (carritoState.exito) {
                        navController.popBackStack()
                    }
                }
            }

            composable("editar_reserva?id={id}")
            { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable

                // Obtener clienteId de sesión
                val clienteId = Session.usuarioActual?.id ?: return@composable
                val viewModelReserva = remember { ReservaAgrupadaViewModel(clienteId) }
                val state by viewModelReserva.state.collectAsState()

                // Obtener la reserva agrupada por códigoTicket
                val reservaAgrupada = state.reservas.find { it.codigoTicket == id }
                if (reservaAgrupada != null) {
                    EditarReservaScreen(
                        navController = navController,
                        reserva = reservaAgrupada
                    )
                }

            }


        }

    }
}


