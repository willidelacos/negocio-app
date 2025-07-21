


package com.tunegocio.negocio.data.remote.usuario



import okhttp3.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


import android.util.Log
import com.tunegocio.negocio.data.remote.usuario.UsuarioMapper

import com.tunegocio.negocio.domain.model.Usuario
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


object UsuarioRemoteDataSource {

    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxlzj4Obq8vCWcsI3G8ikqGw493SUnoiYibDzQgn5mFsW0jUAACFDj0cbX22hsdmvLuhg/exec"
    private const val HOJA = "Usuarios"

    val client = OkHttpClient() // <- Agrega esta línea

    suspend fun listarUsuarios(): List<Usuario> = withContext(Dispatchers.IO) {
        val url = "$BASE_URL?accion=listar&hoja=$HOJA"
        val request = Request.Builder().url(url).get().build()

        try {
            val response = client.newCall(request).execute()
            val usuarios = mutableListOf<Usuario>()

            val jsonStr = response.body?.string()
            if (!jsonStr.isNullOrEmpty()) {
                val jsonArray = JSONObject("{\"datos\": $jsonStr}").getJSONArray("datos")
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    usuarios.add(
                        Usuario(
                            id = item.optString("ID"),
                            nombreUsuario = item.optString("NombreUsuario"),
                            contrasena = item.optString("Contraseña", item.optString("contrasena", item.optString("contraseña", ""))),
                            nombreCompleto = item.optString("NombreCompleto"),
                            telefono = item.optString("Telefono"),
                            rol = item.optString("Rol"),
                            direccion = item.optString("Direccion"),
                            estado = item.optString("Estado"),
                            fechaNacimiento = item.optString("FechaNacimiento"),
                            fechaRegistro = item.optString("FechaRegistro")
                        )
                    )
                }
            }

            Log.d("UsuarioRemote", "Usuarios cargados: ${usuarios.size}")
            usuarios

        } catch (e: Exception) {
            Log.e("UsuarioRemote", "❌ Error al listar usuarios: ${e.message}")
            emptyList()
        }
    }

    // --- AGREGAR USUARIO (POST) ---
    suspend fun agregarUsuario(usuario: Usuario): Boolean = withContext(Dispatchers.IO) {
        val formBodyBuilder = FormBody.Builder()
        UsuarioMapper.toParams(usuario).forEach { (k, v) -> formBodyBuilder.add(k, v) }
        formBodyBuilder.add("accion", "añadir").add("hoja", HOJA)

        val request = Request.Builder()
            .url(BASE_URL)
            .post(formBodyBuilder.build())
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            Log.d("UsuarioRemote", "🟢 Respuesta agregar: $body")
            body?.let { JSONObject(it).optBoolean("error") == false } ?: false
        } catch (e: Exception) {
            Log.e("UsuarioRemote", "❌ Error en agregarUsuario: ${e.message}")
            false
        }
    }



    // --- ACTUALIZAR USUARIO (POST) ---
    suspend fun actualizarUsuario(usuario: Usuario): Boolean = withContext(Dispatchers.IO) {
        val formBodyBuilder = FormBody.Builder()
        UsuarioMapper.toParams(usuario).forEach { (k, v) -> formBodyBuilder.add(k, v) }
        formBodyBuilder.add("accion", "actualizar").add("hoja", HOJA)

        val request = Request.Builder()
            .url(BASE_URL)
            .post(formBodyBuilder.build())
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            Log.d("UsuarioRemote", "🔄 Respuesta actualizar: $body")
            body?.let { JSONObject(it).optBoolean("error") == false } ?: false
        } catch (e: Exception) {
            Log.e("UsuarioRemote", "❌ Error en actualizarUsuario: ${e.message}")
            false
        }
    }


    // --- ELIMINAR USUARIO (POST) ---
    suspend fun eliminarUsuario(id: String): Boolean = withContext(Dispatchers.IO) {
        Log.d("UsuarioRemote", "🧪 Eliminando ID = $id") // <--- AGREGALO

        val formBody = FormBody.Builder()
            .add("accion", "eliminar")
            .add("hoja", HOJA)
            .add("ID", id)
            .build()

        val request = Request.Builder()
            .url(BASE_URL)
            .post(formBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            Log.d("UsuarioRemote", "🗑️ Respuesta eliminar: $body") // <--- IMPORTANTE
            body?.let { JSONObject(it).optBoolean("error") == false } ?: false
        } catch (e: Exception) {
            Log.e("UsuarioRemote", "❌ Error en eliminarUsuario: ${e.message}")
            false
        }
    }


}
