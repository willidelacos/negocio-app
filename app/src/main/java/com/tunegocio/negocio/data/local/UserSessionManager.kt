
/**
 * Clase responsable de gestionar la sesión del usuario usando DataStore.
 * Guarda, obtiene y elimina los datos de sesión del usuario.
 *
 * Importante:
 * - Esta clase es clave para el manejo seguro de la sesión.
 * - No modificar sin coordinación previa.
 * - Usada por LoginViewModel y otros componentes que requieren sesión.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
package com.tunegocio.negocio.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import com.tunegocio.negocio.domain.model.Usuario

private val Context.dataStore by preferencesDataStore("user_session")
class UserSessionManager(private val context: Context) {

    companion object {
        // Claves para guardar y obtener datos en DataStore
        val USER_ID = stringPreferencesKey("usuario_id")
        val USERNAME = stringPreferencesKey("nombre_usuario")
        val CONTRASENA = stringPreferencesKey("contrasena")
        val ROL = stringPreferencesKey("rol")
        val FULLNAME = stringPreferencesKey("nombre_completo")
        val TELEFONO = stringPreferencesKey("telefono")
        val DIRECCION = stringPreferencesKey("direccion")
        val ESTADO = stringPreferencesKey("estado")
        val FECHA_NACIMIENTO = stringPreferencesKey("fecha_nacimiento")
        val FECHA_REGISTRO = stringPreferencesKey("fecha_registro")
    }
    /**
     * Guarda los datos de sesión del usuario en DataStore.
     * @param id Identificador único del usuario
     * @param nombreUsuario Nombre de usuario
     * @param rol Rol del usuario (ej. admin, cliente)
     * @param nombreCompleto Nombre completo del usuario
     * @param telefono Teléfono de contacto
     */

    suspend fun guardarSesion(usuario: Usuario){
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = usuario.id
            prefs[USERNAME] = usuario.nombreUsuario
            prefs[CONTRASENA] = usuario.contrasena
            prefs[ROL] = usuario.rol
            prefs[FULLNAME] = usuario.nombreCompleto
            prefs[TELEFONO] = usuario.telefono
            prefs[DIRECCION] = usuario.direccion
            prefs[ESTADO] = usuario.estado
            prefs[FECHA_NACIMIENTO] = usuario.fechaNacimiento
            prefs[FECHA_REGISTRO] = usuario.fechaRegistro
        }
    }
    /**
     * Obtiene los datos de sesión del usuario almacenados en DataStore.
     * Si no hay usuario logueado retorna null.
     * @return Usuario? con los datos o null si no hay sesión.
     */
suspend fun obtenerUsuarioSesion(): Usuario? {
    val prefs = context.dataStore.data.first()
    val id = prefs[USER_ID]

        // ⚠️ Validamos también si está vacío
        if (id.isNullOrBlank()) return null


        return Usuario(
            id = id,
            nombreUsuario = prefs[USERNAME] ?: "",
            contrasena = prefs[CONTRASENA] ?: "", // ← debes definir esta clave también
            rol = prefs[ROL] ?: "",
            nombreCompleto = prefs[FULLNAME] ?: "",
            telefono = prefs[TELEFONO] ?: "",
            direccion = prefs[DIRECCION] ?: "",
            estado = prefs[ESTADO] ?: "",
            fechaNacimiento = prefs[FECHA_NACIMIENTO] ?: "",
            fechaRegistro = prefs[FECHA_REGISTRO] ?: ""
        )
}
    /**
     * Elimina todos los datos almacenados en la sesión, cerrando sesión.
     */
    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}
