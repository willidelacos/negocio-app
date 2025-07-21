package com.tunegocio.negocio.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tunegocio.negocio.di.AppModule
import com.tunegocio.negocio.utils.Session
import com.tunegocio.negocio.data.local.UserSessionManager
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.ui.Alignment
@Composable
fun LoginScreen(
    navController: NavController,
    userSessionManager: UserSessionManager
) {
    val context = LocalContext.current
    // Inicialización manual del ViewModel sin Hilt para mantener control directo
    val viewModel = remember {
        val userSessionManager = UserSessionManager(context)

        LoginViewModel(
            loginUseCase = AppModule.provideLoginUseCase(),
            userSessionManager = userSessionManager
        )
    }
    // Observamos el estado del ViewModel con Compose StateFlow
    val state by viewModel.state.collectAsState()
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }


    // Al cambiar el usuario logueado, iniciamos sesión y navegamos
    LaunchedEffect(state.usuario) {
        state.usuario?.let {
            Session.iniciarSesion(it)
            navController.navigate("home") {
                popUpTo(0)
            }
        }
    }
    // ✅ Diseño general: centrado, espacioso y moderno
    // UI principal: columna centrada y espaciosa
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), // Más padding para estética
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        // ✅ Título más estilizado
        Text(
            text = "Bienvenido a Panadería Flor 🍞",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Campo usuario con OutlinedTextField
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Nombre de Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Campo contraseña con seguridad y teclado adecuado
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))


        Spacer(modifier = Modifier.height(16.dp))
        // ✅ Botón principal
        Button(
            onClick = {
                if (user.isBlank() || pass.isBlank()) {
                    Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.onEvent(LoginEvent.Submit(user, pass))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Alto fijo para consistencia
        ) {
            Text("Iniciar sesión")
        }

        // Mostrar indicador de carga mientras espera respuesta
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        // Mostrar mensaje de error si ocurre un fallo en login
        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }


    }
}
