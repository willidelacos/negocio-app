package com.tunegocio.negocio.presentation.detalle_reserva

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunegocio.negocio.data.local.UserSessionManager
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import com.tunegocio.negocio.utils.CampoFechaConCalendario
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetalleReservaScreenAgrupada(
    viewModel: ReservaAgrupadaViewModel,
    navController: NavController,
    onEditar: (ReservaConProductos) -> Unit,
    onEliminar: (ReservaConProductos) -> Unit,
    onWhatsApp: (ReservaConProductos) -> Unit,
    onReservar: (ReservaConProductos) -> Unit,
    onRecoger: (ReservaConProductos) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    var fechaSeleccionada by remember { mutableStateOf(sdf.format(Date())) }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    val rolUsuario = remember { mutableStateOf("cliente") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarPorFecha(fechaSeleccionada)

        val usuario = UserSessionManager(context).obtenerUsuarioSesion()
        rolUsuario.value = usuario?.rol?.lowercase() ?: "cliente"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp)) {

            Text("Filtrar por fecha", style = MaterialTheme.typography.titleMedium)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    val hoy = sdf.format(Date())
                    fechaSeleccionada = hoy
                    viewModel.cargarPorFecha(hoy)
                }) { Text("Hoy") }

                Button(onClick = {
                    val c = Calendar.getInstance()
                    DatePickerDialog(context, { _, y, m, d ->
                        val f = "$y-${(m + 1).toString().padStart(2, '0')}-${d.toString().padStart(2, '0')}"
                        fechaSeleccionada = f
                        viewModel.cargarPorFecha(f)
                    }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]).show()
                }) { Text("Elegir fecha") }
            }

            Spacer(Modifier.height(8.dp))

            Text("Buscar por rango de fechas")

            Row {
                CampoFechaConCalendario(
                    label = "Inicio",
                    fecha = fechaInicio,
                    onFechaSeleccionada = { fechaInicio = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                CampoFechaConCalendario(
                    label = "Fin",
                    fecha = fechaFin,
                    onFechaSeleccionada = { fechaFin = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    if (fechaInicio.isNotBlank() && fechaFin.isNotBlank()) {
                        if (fechaInicio > fechaFin) {
                            Toast.makeText(context, "Rango inválido", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.cargarPorRango(fechaInicio, fechaFin)
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Buscar")
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(state.reservas) { reserva ->
                        val cardColor = when (reserva.estado) {
                            "Pendiente" -> Color(0xFFFFF59D)
                            "Reservado" -> Color(0xFFBBDEFB)
                            "Aceptado", "Modificado" -> Color(0xFFD1C4E9)
                            "Recogido" -> Color(0xFFC8E6C9)
                            "Eliminado" -> Color(0xFFE0E0E0)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(3.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🎫 Ticket: ${reserva.codigoTicket}", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text("📅 Reserva: ${reserva.fechaReserva}", style = MaterialTheme.typography.bodySmall)
                                Text("🧾 Estado: ${reserva.estado}", style = MaterialTheme.typography.bodySmall)
                                Text("🧍 Cliente: ${reserva.nombreCompleto} (${reserva.nombreUsuario})", style = MaterialTheme.typography.bodySmall)
                                Text("📞 Tel: ${reserva.telefono}", style = MaterialTheme.typography.bodySmall)

                                Spacer(Modifier.height(8.dp))

                                Text("🧺 Productos:", style = MaterialTheme.typography.titleSmall)
                                reserva.productos.forEach {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("- ${it.nombreProducto} x${it.cantidad}", style = MaterialTheme.typography.bodySmall)
                                        Text("Bs ${"%.2f".format(it.subtotal)}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }

                                Spacer(Modifier.height(8.dp))
                                Text("💵 Total: Bs ${"%.2f".format(reserva.productos.sumOf { it.subtotal })}", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))

                                when (rolUsuario.value) {
                                    "cliente" -> {
                                        when (reserva.estado) {
                                            "Pendiente" -> {
                                                Row {
                                                    Button(
                                                        onClick = { onEditar(reserva) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                                                    ) {
                                                        Text("✏️ Editar", color = Color.White)
                                                    }

                                                    Spacer(Modifier.width(8.dp))
                                                    Button(onClick = { onWhatsApp(reserva) }) { Text("📲 WhatsApp") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(
                                                        onClick = { onEliminar(reserva) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                    ) {
                                                        Text("🗑️ Eliminar")
                                                    }
                                                }
                                            }
                                            "Modificado", "Reservado" -> {
                                                Row {
                                                    Button(onClick = { onWhatsApp(reserva) }) { Text("📲 WhatsApp") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(
                                                        onClick = { onEliminar(reserva) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                    ) {
                                                        Text("🗑️ Eliminar")
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    "admin" -> {
                                        when (reserva.estado) {
                                            "Pendiente", "Modificado" -> {
                                                Row {
                                                    Button(onClick = { onEditar(reserva) }) { Text("✏️ Editar") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(onClick = { onReservar(reserva) }) { Text("✅ Reservar") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(
                                                        onClick = { onEliminar(reserva) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                    ) {
                                                        Text("🗑️ Eliminar")
                                                    }
                                                }
                                            }
                                            "Reservado" -> {
                                                Row {
                                                    Button(onClick = { onWhatsApp(reserva) }) { Text("📲 WhatsApp") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(onClick = { onRecoger(reserva) }) { Text("✅ Recoger") }
                                                    Spacer(Modifier.width(8.dp))
                                                    Button(
                                                        onClick = { onEliminar(reserva) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                    ) {
                                                        Text("🗑️ Eliminar")
                                                    }
                                                }
                                            }
                                            "Aceptado" -> {
                                                Button(
                                                    onClick = { onEliminar(reserva) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                ) {
                                                    Text("🗑️ Eliminar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        FloatingActionButton(
            onClick = { navController.navigate("carrito") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}


