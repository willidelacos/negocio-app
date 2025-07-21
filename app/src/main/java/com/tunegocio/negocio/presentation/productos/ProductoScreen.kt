package com.tunegocio.negocio.presentation.productos

// AndroidX - Compose UI
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Coil
import coil.compose.rememberAsyncImagePainter

// Proyecto
import com.tunegocio.negocio.domain.model.Producto

import android.util.Log
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
@Composable
fun ProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProductoEvent.CargarProductos)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("producto_form")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = "❌ Error: ${state.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.productos.isEmpty() -> {
                    Text(
                        text = "No hay productos registrados.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(state.productos) { producto ->
                            ProductoItem(
                                producto = producto,
                                onEditar = {
                                    navController.navigate("producto_form?id=${producto.id}")
                                },
                                onEliminar = {
                                    viewModel.onEvent(ProductoEvent.EliminarProducto(producto.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ProductoItem(
    producto: Producto,
    onEditar: (Producto) -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(producto.imagenUrl),
                contentDescription = "Imagen del producto",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
            Log.d("ProductoScreen", "URL imagen: ${producto.imagenUrl}")
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombreProducto, style = MaterialTheme.typography.titleMedium)
                Text(text = "Bs ${producto.precio} | Stock: ${producto.stock}")
            }
            IconButton(onClick = { onEditar(producto) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}