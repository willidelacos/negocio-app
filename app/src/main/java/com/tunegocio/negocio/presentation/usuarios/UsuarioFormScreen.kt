package com.tunegocio.negocio.presentation.usuarios

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunegocio.negocio.domain.model.Usuario
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.tunegocio.negocio.utils.SecurityUtils

@Composable
fun UsuarioFormScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    usuario: Usuario? = null
) {var id by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var nombreCompleto by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var fechaRegistro by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()

    LaunchedEffect(usuario) {
        usuario?.let {
            id = it.id
            nombreUsuario = it.nombreUsuario
            nombreCompleto = it.nombreCompleto
            rol = it.rol
            telefono = it.telefono
            direccion = it.direccion
            estado = it.estado
            fechaNacimiento = it.fechaNacimiento
            contrasena = ""
            fechaRegistro = it.fechaRegistro

        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = if (usuario == null) "Registrar Usuario" else "Editar Usuario",
            style = MaterialTheme.typography.titleLarge
        )

        if (usuario != null) {
            OutlinedTextField(
                value = id,
                onValueChange = {},
                label = { Text("ID") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nombreCompleto,
            onValueChange = { nombreCompleto = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rol,
            onValueChange = { rol = it },
            label = { Text("Rol") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado,
            onValueChange = { estado = it },
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { fechaNacimiento = it },
            label = { Text("Fecha nacimiento") },
            modifier = Modifier.fillMaxWidth()
        )
        if (usuario != null) {
            OutlinedTextField(
                value = fechaRegistro,
                onValueChange = {},
                label = { Text("Fecha de registro") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (nombreUsuario.isBlank() || nombreCompleto.isBlank() || rol.isBlank()) {
                Toast.makeText(context, "⚠️ Completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
                return@Button
            }



            val idGenerado = System.currentTimeMillis().toString() + (1000..9999).random()
            val esEdicion = usuario != null
            Log.d("ID_GENERADO", "ID: $idGenerado, length: ${idGenerado.length}")
            val nuevoUsuario = Usuario(
                id = if (usuario != null) usuario.id else idGenerado,
                nombreUsuario = nombreUsuario,
                nombreCompleto = nombreCompleto,
                rol = rol,
                telefono = telefono,
                direccion = direccion,
                estado = estado,
                fechaNacimiento = fechaNacimiento,
                fechaRegistro = if (esEdicion) fechaRegistro else LocalDate.now().toString(),
                contrasena = SecurityUtils.hashPassword(contrasena)
            )

            Log.d("Formulario", "Enviando usuario: $nuevoUsuario")

            if (esEdicion) {
                viewModel.onEvent(UsuarioEvent.Actualizar(nuevoUsuario))
            } else {
                viewModel.onEvent(UsuarioEvent.Agregar(nuevoUsuario))
            }

            navController.popBackStack()
        }) {
            Text("Guardar")
        }

    }
}
