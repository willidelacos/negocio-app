package com.tunegocio.negocio.presentation.usuarios

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.presentation.usuarios.UsuarioEvent
import com.tunegocio.negocio.presentation.usuarios.UsuarioViewModel
import com.tunegocio.negocio.presentation.usuarios.UsuarioState
import androidx.compose.ui.Alignment
@Composable
fun UsuarioScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    }
    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text("Lista de Usuarios", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Error: ${state.error}", color = Color.Red)
                state.usuarios.isEmpty() -> Text("No hay usuarios")
                else -> LazyColumn {
                    items(state.usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Usuario: ${usuario.nombreUsuario}")
                                Text("Nombre: ${usuario.nombreCompleto}")
                                Text("Rol: ${usuario.rol}")

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(onClick = {
                                        navController.navigate("usuarioForm/${usuario.id}")
                                    }) {
                                        Text("Editar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        viewModel.onEvent(UsuarioEvent.Eliminar(usuario.id))
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 👉 FAB (botón flotante) va **dentro del Box**, al final
        FloatingActionButton(
            onClick = { navController.navigate("usuarioForm") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}
