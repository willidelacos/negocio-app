package com.tunegocio.negocio.utils


import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun CampoFechaConCalendario(
    label: String,
    fecha: String,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    OutlinedTextField(
        value = fecha,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                mostrarDatePicker(context, calendar) { fechaSeleccionada ->
                    onFechaSeleccionada(fechaSeleccionada)
                }
            },
        trailingIcon = {
            Text(
                text = "📅",
                modifier = Modifier.clickable {
                    mostrarDatePicker(context, calendar) { fechaSeleccionada ->
                        onFechaSeleccionada(fechaSeleccionada)
                    }
                }
            )
        }
    )
}

private fun mostrarDatePicker(
    context: Context,
    calendar: Calendar,
    onFechaSeleccionada: (String) -> Unit
) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, y, m, d ->
        val f = "%04d-%02d-%02d".format(y, m + 1, d)
        onFechaSeleccionada(f)
    }, year, month, day).show()
}
