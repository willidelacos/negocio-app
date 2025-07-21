package com.tunegocio.negocio.data.remote.login

import com.tunegocio.negocio.data.remote.GoogleSheetsService1
import com.tunegocio.negocio.domain.model.Usuario
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.io.BufferedReader
import java.io.InputStreamReader



object LoginRemoteDataSource {

    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxlzj4Obq8vCWcsI3G8ikqGw493SUnoiYibDzQgn5mFsW0jUAACFDj0cbX22hsdmvLuhg/exec"
    /**
     * Función que realiza el login enviando usuario y clave (hash)
     * al backend para validar credenciales.
     *
     * @param nombreUsuario Nombre de usuario para login
     * @param claveHash Clave hasheada para validar
     * @return JSONObject con datos del usuario si es exitoso, o null si falla
     */
    fun login(nombreUsuario: String, claveHash: String): JSONObject? {
        return try {
            //println("Login attempt -> usuario: $nombreUsuario, password hash: $claveHash")
            val urlParams = "accion=login" +
                    "&hoja=${URLEncoder.encode("Usuarios", "UTF-8")}" +
                    "&usuario=${URLEncoder.encode(nombreUsuario, "UTF-8")}" +
                    "&password=${URLEncoder.encode(claveHash, "UTF-8")}"


            val url = URL("$BASE_URL?$urlParams")
            // println("pagina -> usuario: $url")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()
                return JSONObject(response)
            } else null

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
