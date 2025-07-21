package com.tunegocio.negocio.data.remote.producto

import android.util.Log
import com.tunegocio.negocio.domain.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
//para cargar imagen
import com.tunegocio.negocio.util.transformarLinkDrive

object ProductoRemoteDataSource {

    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbxlzj4Obq8vCWcsI3G8ikqGw493SUnoiYibDzQgn5mFsW0jUAACFDj0cbX22hsdmvLuhg/exec"
    private const val HOJA = "Productos"

    private suspend fun getJsonArray(url: String): JSONArray? = withContext(Dispatchers.IO) {
        return@withContext try {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.connect()

            val response = conn.inputStream.bufferedReader().use(BufferedReader::readText)
            JSONArray(response)
        } catch (e: Exception) {
            Log.e("HttpGet", "❌ Error en getJsonArray: ${e.message}")
            null
        }
    }

    private suspend fun postJson(url: String, jsonObject: JSONObject): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val outputStream = OutputStreamWriter(conn.outputStream)
            outputStream.write(jsonObject.toString())
            outputStream.flush()
            outputStream.close()

            val responseCode = conn.responseCode
            Log.d("HttpPost", "✅ Código de respuesta: $responseCode")

            responseCode == 200
        } catch (e: Exception) {
            Log.e("HttpPost", "❌ Error en postJson: ${e.message}")
            false
        }
    }

    suspend fun listarProductos(): List<Producto> {
        val url = "$BASE_URL?hoja=$HOJA&accion=listar"
        val jsonArray = getJsonArray(url) ?: return emptyList()
        return (0 until jsonArray.length()).mapNotNull { i ->
            try {
                val obj = jsonArray.getJSONObject(i)
                Producto(
                    id = obj.optString("ID"),
                    nombreProducto = obj.optString("NombreProducto"),
                    descripcion = obj.optString("Descripción"),
                    precio = obj.optDouble("Precio", 0.0),
                    stock = obj.optInt("Stock", 0),
                    imagenUrl = transformarLinkDrive(obj.optString("ImagenURL")),
                    activo = obj.optString("Activo").equals("Sí", ignoreCase = true),
                    recompezas = obj.optInt("Recompezas", 0)
                )
            } catch (e: Exception) {
                Log.e("ProductoRemote", "❌ Error parseando producto: ${e.message}")
                null
            }
        }
    }

    suspend fun agregarProducto(producto: Producto): Boolean {
        val body = JSONObject().apply {
            put("hoja", HOJA)
            put("accion", "añadir")
            put("NombreProducto", producto.nombreProducto)
            put("Descripción", producto.descripcion)
            put("Precio", producto.precio.toString())
            put("Stock", producto.stock.toString())
            put("ImagenURL", producto.imagenUrl)
            put("Activo", if (producto.activo) "Sí" else "No")
            put("Recompezas", producto.recompezas.toString())
        }
        return postJson(BASE_URL, body)
    }

    suspend fun actualizarProducto(producto: Producto): Boolean {
        val body = JSONObject().apply {
            put("hoja", HOJA)
            put("accion", "actualizar")
            put("ID", producto.id)
            put("NombreProducto", producto.nombreProducto)
            put("Descripción", producto.descripcion)
            put("Precio", producto.precio.toString())
            put("Stock", producto.stock.toString())
            put("ImagenURL", producto.imagenUrl)
            put("Activo", if (producto.activo) "Sí" else "No")
            put("Recompezas", producto.recompezas.toString())
        }
        return postJson(BASE_URL, body)
    }

    suspend fun eliminarProducto(id: String): Boolean {
        val body = JSONObject().apply {
            put("hoja", HOJA)
            put("accion", "eliminar")
            put("ID", id)
        }
        return postJson(BASE_URL, body)
    }
    private fun transformarLinkDrive(link: String): String {
        val regex = Regex("""/d/([a-zA-Z0-9_-]+)""")
        val match = regex.find(link)
        val fileId = match?.groups?.get(1)?.value
        return if (fileId != null) {
            "https://drive.google.com/uc?export=download&id=$fileId"
        } else {
            link
        }
    }

}
