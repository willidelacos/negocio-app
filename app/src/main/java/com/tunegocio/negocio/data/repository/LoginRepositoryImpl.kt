
/**
 * Implementación del repositorio de Login.
 *
 * Se encarga de realizar la llamada al servicio externo (GoogleSheetsService)
 * para validar las credenciales del usuario mediante Google Sheets.
 *
 * Ejecuta la llamada en un contexto IO para no bloquear el hilo principal.
 *
 * Importante:
 * - El password debe estar hasheado antes de ser enviado (ya se recibe hasheado).
 * - El formato de la respuesta JSON debe respetar la estructura esperada:
 *   un objeto con un booleano "error" y un objeto "datos" con la info del usuario.
 * - Retorna un objeto Usuario si el login es exitoso, o null si falla.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */

package com.tunegocio.negocio.data.repository

import com.tunegocio.negocio.data.remote.GoogleSheetsService1
import com.tunegocio.negocio.domain.model.Usuario
import com.tunegocio.negocio.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.tunegocio.negocio.data.remote.login.LoginRemoteDataSource
import com.tunegocio.negocio.utils.SecurityUtils

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(nombreUsuario: String, clave: String): Usuario? {
        return withContext(Dispatchers.IO) {
            // Llamada al backend para validar usuario y clave


            val response = LoginRemoteDataSource.login(nombreUsuario, clave)

            if (response != null && response.optBoolean("error") == false) {
                val data: JSONObject = response.getJSONObject("datos")

                return@withContext Usuario(
                    id = data.optString("ID"),
                    nombreUsuario = data.optString("NombreUsuario"),
                    contrasena = data.optString("Contrasena"),
                    rol = data.optString("Rol"),
                    nombreCompleto = data.optString("NombreCompleto"),
                    telefono = data.optString("Telefono"),
                    direccion = data.optString("Direccion"),
                    estado = data.optString("Estado"),
                    fechaNacimiento = data.optString("FechaNacimiento"),
                    fechaRegistro = data.optString("FechaRegistro")
                )
            } else return@withContext null
        }
    }
}
