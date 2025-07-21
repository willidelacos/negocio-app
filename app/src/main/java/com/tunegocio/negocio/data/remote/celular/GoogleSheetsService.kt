package com.tunegocio.negocio.data.remote.celular

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GoogleSheetsService {

    companion object {
        private const val BASE_URL =
            "https://script.google.com/macros/s/AKfycbxlzj4Obq8vCWcsI3G8ikqGw493SUnoiYibDzQgn5mFsW0jUAACFDj0cbX22hsdmvLuhg/exec"
        private const val TAG = "GoogleSheetsService"

        /**
         * Obtiene el número de WhatsApp almacenado en la hoja 'Celular'
         * desde el backend de Apps Script.
         *
         * @return número como string o null si falla.
         */
        suspend fun obtenerNumeroWhatsApp(): String? {
            val url = "$BASE_URL?accion=getNumeroWhatsApp"
            return try {
                val json = getJsonObject(url)
                json.optString("celular", "")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener número de WhatsApp", e)
                null
            }
        }

        /**
         * Realiza una solicitud GET a la URL y devuelve la respuesta como JSONObject.
         */
        fun getJsonObject(urlStr: String): JSONObject {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            return try {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()
                JSONObject(response)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en GET: $urlStr", e)
                JSONObject()
            } finally {
                connection.disconnect()
            }
        }
    }
}
