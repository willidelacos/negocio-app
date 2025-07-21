package com.tunegocio.negocio.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunegocio.negocio.utils.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tunegocio.negocio.data.local.UserSessionManager

@Composable
fun HomeScreen(navController: NavController,
               userSessionManager: UserSessionManager)
{
    val usuario = Session.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Bienvenido,", style = MaterialTheme.typography.headlineSmall)
            Text(usuario?.nombreCompleto ?: "Usuario desconocido", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rol: ${usuario?.rol}", style = MaterialTheme.typography.bodyMedium)
        }

        //usuarios
        // 🔹 Botón para navegar a Usuarios
        Button(
            onClick = { navController.navigate("usuarios") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Usuarios")
        }

        //usuarios
        // 🔹 Botón para navegar a Usuarios
        Button(
            onClick = { navController.navigate("productos") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Productos")
        }
        Button(
            onClick = { navController.navigate("detalle_reservas_agrupado") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("📋 Mis Pedidos Agrupados")
        }


//cerrar sesión
        Button(
            onClick = {
                Session.cerrarSesion()

                // 🔹 Limpia la sesión persistente (DataStore)
                CoroutineScope(Dispatchers.IO).launch {
                    userSessionManager.cerrarSesion()

                    withContext(Dispatchers.Main) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true } // Limpia el backstack
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
