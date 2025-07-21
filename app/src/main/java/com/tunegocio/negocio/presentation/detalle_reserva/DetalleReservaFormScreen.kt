package com.tunegocio.negocio.presentation.detalle_reserva

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva

@Composable
fun DetalleReservaFormScreen(
    reserva: DetalleReserva?,
    onGuardar: (DetalleReserva) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    var fechaEntrega by remember { mutableStateOf(reserva?.fechaEntrega ?: "") }
    var horaEntrega by remember { mutableStateOf(reserva?.horaEntrega ?: "") }
    var tipoPago by remember { mutableStateOf(reserva?.tipoPago ?: "") }
    var observaciones by remember { mutableStateOf(reserva?.observaciones ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        OutlinedTextField(
            value = fechaEntrega,
            onValueChange = { fechaEntrega = it },
            label = { Text("Fecha Entrega") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = horaEntrega,
            onValueChange = { horaEntrega = it },
            label = { Text("Hora Entrega") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tipoPago,
            onValueChange = { tipoPago = it },
            label = { Text("Tipo Pago") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = observaciones,
            onValueChange = { observaciones = it },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val actualizada = reserva?.copy(
                    fechaEntrega = fechaEntrega.trim(),
                    horaEntrega = horaEntrega.trim(),
                    tipoPago = tipoPago.trim(),
                    observaciones = observaciones.trim()
                )
                if (actualizada != null) {
                    onGuardar(actualizada)
                    onVolver()
                } else {
                    Toast.makeText(context, "Reserva inválida", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}
