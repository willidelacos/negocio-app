package com.tunegocio.negocio.presentation.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunegocio.negocio.domain.usecase.UsuarioUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tunegocio.negocio.domain.model.Usuario
import android.util.Log

class UsuarioViewModel(
    private val useCases: UsuarioUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(UsuarioState())
    val state: StateFlow<UsuarioState> = _state

    init {
        // Esto dispara automáticamente la carga al crear el ViewModel
        onEvent(UsuarioEvent.LoadUsuarios)
    }

    fun onEvent(event: UsuarioEvent) {
        when (event) {
            is UsuarioEvent.LoadUsuarios -> cargarUsuarios()
            is UsuarioEvent.Agregar -> agregarUsuario(event.usuario)
            is UsuarioEvent.Actualizar -> actualizarUsuario(event.usuario)
            is UsuarioEvent.Eliminar -> eliminarUsuario(event.id)
        }
    }

    private fun cargarUsuarios() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val lista = useCases.obtenerUsuarios()
                _state.value = _state.value.copy(isLoading = false, usuarios = lista)
                Log.d("UsuarioViewModel", "Usuarios cargados: ${lista.size}")
                lista.forEach { Log.d("UsuarioViewModel", "Usuario: ${it.nombreUsuario}") }

            } catch (e: Exception) {
                manejarError("Error al obtener usuarios")
            }
        }
    }

    private fun validarUsuario(usuario: Usuario): String? {
        if (usuario.nombreUsuario.isBlank()) return "Usuario requerido"
        if (usuario.nombreCompleto.isBlank()) return "Nombre completo requerido"
        if (usuario.telefono.isBlank()) return "Teléfono requerido"
        if (usuario.rol.isBlank()) return "Rol requerido"
        if (usuario.direccion.isBlank()) return "Dirección requerida"
        if (usuario.estado.isBlank()) return "Estado requerido"
        if (usuario.fechaNacimiento.isBlank()) return "Fecha de nacimiento requerida"
        return null
    }

//inicio ok
private fun agregarUsuario(usuario: Usuario) {
    viewModelScope.launch {
        try {
            val exito = useCases.agregarUsuario(usuario)
            if (exito) cargarUsuarios()
            else manejarError("Error al agregar usuario")
        } catch (e: Exception) {
            manejarError("Error al agregar usuario")
        }
    }
}

//fin ok

    private fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            val exito = useCases.actualizarUsuario(usuario)
            if (exito) {
                _state.value = _state.value.copy(mensaje = "Usuario actualizado correctamente")
                cargarUsuarios()
            } else {
                manejarError("Error al actualizar usuario")
            }
        }
    }


    private fun eliminarUsuario(id: String) {
        viewModelScope.launch {
            try {
                val exito = useCases.eliminarUsuario(id)
                if (exito) {
                    _state.value = _state.value.copy(mensaje = "Usuario eliminado correctamente")
                    Log.d("UsuarioViewModel", "🧪 ID a eliminar: $id")
                    cargarUsuarios()
                }
                else manejarError("Error al eliminar usuario")
            } catch (e: Exception) {
                manejarError("Error al eliminar usuario")
            }
        }
    }

    private fun manejarError(mensaje: String) {
        _state.value = _state.value.copy(isLoading = false, error = mensaje)
    }




}
