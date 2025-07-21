package com.tunegocio.negocio.presentation.productos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tunegocio.negocio.domain.model.Producto
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
@Composable
fun ProductoFormScreen(
    navController: NavController,
    viewModel: ProductoViewModel,
    producto: Producto? = null
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }
    var recompezas by remember { mutableStateOf("0") }


    fun validarCampos(
        nombre: String,
        descripcion: String,
        precio: String,
        stock: String,
        imagenUrl: String
    ): String? {
        return when {
            nombre.isBlank() -> "⚠️ El nombre es obligatorio"
            descripcion.isBlank() -> "⚠️ La descripción es obligatoria"
            precio.toDoubleOrNull() == null || precio.toDouble() <= 0 -> "⚠️ Precio inválido"
            stock.toIntOrNull() == null || stock.toInt() < 0 -> "⚠️ Stock inválido"
            imagenUrl.isBlank() -> "⚠️ La URL de imagen es obligatoria"
            else -> null
        }
    }


    LaunchedEffect(producto) {
        producto?.let {
            nombre = it.nombreProducto
            descripcion = it.descripcion
            precio = it.precio.toString()
            stock = it.stock.toString()
            imagenUrl = it.imagenUrl
            activo = it.activo
            recompezas = it.recompezas.toString()
        }
    }

    val esEdicion = producto != null

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (esEdicion) "Editar Producto" else "Registrar Producto",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("URL de Imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = recompezas,
            onValueChange = { recompezas = it },
            label = { Text("Recompezas") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = activo, onCheckedChange = { activo = it })
            Text("Activo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancelar")
            }

            Button(onClick = {


                val error = validarCampos(nombre, descripcion, precio, stock, imagenUrl)
                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val nuevo = Producto(
                    id = if (esEdicion) producto!!.id else System.currentTimeMillis().toString(),
                    nombreProducto = nombre.trim(),
                    descripcion = descripcion.trim(),
                    precio = precio.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    stock = stock.toInt(),
                    imagenUrl = imagenUrl.trim(),
                    activo = activo,
                    recompezas = recompezas.toIntOrNull() ?: 0
                )

                if (esEdicion) {
                    viewModel.onEvent(ProductoEvent.ActualizarProducto(nuevo))
                } else {
                    viewModel.onEvent(ProductoEvent.AgregarProducto(nuevo))
                }
                navController.popBackStack()
            }) {
                Text(if (esEdicion) "Actualizar" else "Guardar")
            }

        }

    }



}
