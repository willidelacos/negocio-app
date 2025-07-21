package com.tunegocio.negocio.presentation.detalle_reserva


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import com.tunegocio.negocio.domain.model.reserva.ItemCarrito
import com.tunegocio.negocio.domain.model.Producto
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Delete


@Composable
fun CarritoScreen(
    productosDisponibles: List<Producto>,
    carrito: List<ItemCarrito>,
    onCantidadChange: (ItemCarrito) -> Unit,
    onEliminarItem: (ItemCarrito) -> Unit,
    onConfirmar: (List<ItemCarrito>) -> Unit,
    onCancelar: () -> Unit
) {
    var observaciones by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Productos disponibles", style = MaterialTheme.typography.titleMedium)

        if (productosDisponibles.isEmpty()) {
            Text("No hay productos disponibles", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productosDisponibles) { producto ->
                    Log.d("UI", "🔹 Mostrando: ${producto.nombreProducto}")
                    Button(
                        onClick = {
                            val existente = carrito.find { it.productoId == producto.id }
                            if (existente == null) {
                                onCantidadChange(
                                    ItemCarrito(
                                        productoId = producto.id,
                                        nombreProducto = producto.nombreProducto,
                                        cantidad = 1,
                                        precioUnitario = producto.precio,
                                        stockDisponible = producto.stock
                                    )
                                )
                            }
                        }
                    ) {
                        Text(producto.nombreProducto)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Carrito de compras",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))
//inicio
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {


            items(carrito) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    val stockRestante = item.stockDisponible - item.cantidad
                    Column(modifier = Modifier.padding(10.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Column {
                                    Text(item.nombreProducto, style = MaterialTheme.typography.bodyMedium)
                                    Text("Bs ${"%.2f".format(item.precioUnitario)} c/u", style = MaterialTheme.typography.bodySmall)
                                    Text("Stock: ${item.stockDisponible}", style = MaterialTheme.typography.labelSmall)
                                }

                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val nuevaCantidad = (item.cantidad - 1).coerceAtLeast(1)
                                        onCantidadChange(item.copy(cantidad = nuevaCantidad))
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("-", style = MaterialTheme.typography.bodySmall)
                                }

                                OutlinedTextField(
                                    value = item.cantidad.toString(),
                                    onValueChange = {
                                        val nuevaCantidad = it.toIntOrNull()
                                        if (nuevaCantidad != null && nuevaCantidad in 1..item.stockDisponible) {
                                            onCantidadChange(item.copy(cantidad = nuevaCantidad))
                                        }
                                    },
                                    modifier = Modifier
                                        .width(72.dp)
                                        .padding(horizontal = 4.dp),
                                    singleLine = true,
                                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                    //keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )

                                OutlinedButton(
                                    onClick = {
                                        val nuevaCantidad = (item.cantidad + 1).coerceAtMost(item.stockDisponible)
                                        if (item.cantidad < item.stockDisponible) {
                                            val nuevaCantidad = item.cantidad + 1
                                            onCantidadChange(item.copy(cantidad = nuevaCantidad))
                                        }

                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("+", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            Text(
                                text = "Bs ${"%.2f".format(item.subtotal)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            //Text("Stock restante: $stockRestante", style = MaterialTheme.typography.labelSmall)


                            IconButton(onClick = { onEliminarItem(item) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = observaciones,
            onValueChange = { observaciones = it },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val total = carrito.sumOf { it.subtotal }
            Text("Total: Bs ${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)

            Button(onClick = { onConfirmar(carrito) }) {
                Text("Confirmar")
            }
        }
    }
}
