package com.tunegocio.negocio.data.remote.detalle_reserva
import android.util.Log
import com.tunegocio.negocio.domain.model.reserva.ItemCarrito
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import com.tunegocio.negocio.domain.model.Producto
import com.tunegocio.negocio.data.remote.detalle_reserva.DetalleReservaMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import com.tunegocio.negocio.data.remote.producto.ProductoRemoteDataSource
import java.net.URLEncoder
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
object DetalleReservaRemoteDataSource {

    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbxlzj4Obq8vCWcsI3G8ikqGw493SUnoiYibDzQgn5mFsW0jUAACFDj0cbX22hsdmvLuhg/exec"

    suspend fun listarReservasPorClienteYFecha(
        clienteId: String,
        fechaReserva: String
    ): List<ReservaConProductos> = withContext(Dispatchers.IO) {
        try {

            val url = BASE_URL +
                    "?accion=listarPorClienteYFecha" +
                    "&clienteId=${URLEncoder.encode(clienteId, "UTF-8")}" +
                    "&fechaReserva=${URLEncoder.encode(fechaReserva, "UTF-8")}"

            Log.d("ReservaAPI", "📤 URL: $url")

            val response = URL(url).readText()
            Log.d("ReservaAPI", "📨 Response: $response")

            val jsonArray = JSONArray(response)

            val productos = ProductoRemoteDataSource.listarProductos()
            return@withContext agruparPorTicket(jsonArray, productos)

        } catch (e: Exception) {
            Log.e("ReservaAPI", "❌ Error listarReservasPorClienteYFecha: ${e.message}", e)
            emptyList()
        }
    }


    suspend fun listarReservasPorClienteYRango(
        clienteId: String,
        fechaInicio: String,
        fechaFin: String
    ): List<ReservaConProductos> = withContext(Dispatchers.IO) {
        try {
            val url = BASE_URL +
                    "?accion=listarPorClienteYRango" +
                    "&clienteId=${URLEncoder.encode(clienteId, "UTF-8")}" +
                    "&fechaInicio=${URLEncoder.encode(fechaInicio, "UTF-8")}" +
                    "&fechaFin=${URLEncoder.encode(fechaFin, "UTF-8")}"

            Log.d("ReservaAPI", "📤 URL: $url")

            val response = URL(url).readText()
            Log.d("ReservaAPI", "📨 Response: $response")

            val jsonArray = JSONArray(response)

            val productos = ProductoRemoteDataSource.listarProductos()
            return@withContext agruparPorTicket(jsonArray, productos)

        } catch (e: Exception) {
            Log.e("ReservaAPI", "❌ Error listarReservasPorClienteYRango: ${e.message}", e)
            emptyList()
        }
    }


    suspend fun registrarCarrito(
        carrito: List<ItemCarrito>,
        clienteId: String,
        nombreCompleto: String,
        nombreUsuario: String,
        telefono: String,
        fechaReserva: String,
        estado: String
    ): Boolean {
        val ticket = "TICKET-${System.currentTimeMillis()}${(1000..9999).random()}"


        val hoja = "DetalleReserva"

        val registros = carrito.map { item ->
            mapOf(
                "ID" to "${System.currentTimeMillis()}${(1000..9999).random()}",
                "ReservaID" to ticket,
                "CodigoTicket" to ticket,
                "ClienteID" to clienteId,
                "NombreCompleto" to nombreCompleto,
                "NombreUsuario" to nombreUsuario,
                "Telefono" to telefono,
                "FechaReserva" to fechaReserva,
                "ProductoID" to item.productoId,
                "NombreProducto" to item.nombreProducto,
                "Cantidad" to item.cantidad.toString(),
                "Subtotal" to item.subtotal.toString(),
                "Estado" to estado
            )
        }

        val payload = JSONObject().apply {
            put("accion", "añadirMultiple")
            put("hoja", hoja)
            put("registros", JSONArray(registros))
        }

        val response = postJsonObject(payload)
        return response?.optBoolean("error") == false
    }

    private fun agruparPorTicket(
        jsonArray: JSONArray,
        productos: List<Producto>
    ): List<ReservaConProductos> {
        val agrupado = mutableMapOf<String, MutableList<JSONObject>>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val reserva = DetalleReservaMapper.fromJson(item)

            if (reserva.estado == "Eliminado") continue
            val ticket = reserva.codigoTicket
            agrupado.getOrPut(ticket) { mutableListOf() }.add(item)
        }

        return agrupado.map { (ticket, items) ->
            val reserva = DetalleReservaMapper.fromJson(items.first())
            val productosReserva = items.map { item ->
                val productoId = item.optString("ProductoID")
                val cantidad = item.optString("Cantidad").toIntOrNull() ?: 0
                val subtotal = item.optString("Subtotal").toDoubleOrNull() ?: 0.0
                val precioUnitario = if (cantidad > 0) subtotal / cantidad else 0.0

                val stockDisponible = productos.find { it.id == productoId }?.stock ?: 0

                ItemCarrito(
                    productoId = productoId,
                    nombreProducto = item.optString("NombreProducto"),
                    cantidad = cantidad,
                    precioUnitario = precioUnitario,
                    stockDisponible = stockDisponible // ✅ ahora sí existe
                )
            }

            ReservaConProductos(
                codigoTicket = ticket,
                clienteId = reserva.clienteId,
                nombreCompleto = reserva.nombreCompleto,
                nombreUsuario = reserva.nombreUsuario,
                telefono = reserva.telefono,
                fechaReserva = reserva.fechaReserva,
                fechaEntrega = reserva.fechaEntrega,
                horaEntrega = reserva.horaEntrega,
                tipoPago = reserva.tipoPago,
                estado = reserva.estado,
                observaciones = reserva.observaciones,
                puntosGanados = reserva.puntosGanados,
                canjeado = reserva.canjeado,
                productos = productosReserva
            )

        }
    }


    private suspend fun getJsonArray(url: String): JSONArray? = withContext(Dispatchers.IO) {
        return@withContext try {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.connect()

            val response = conn.inputStream.bufferedReader().use(BufferedReader::readText)
            Log.d("ReservaAPI", "📨 Response: $response") // ✅ Mantén el log
            JSONArray(response)
        } catch (e: Exception) {
            Log.e("HttpGet", "❌ Error en getJsonArray: ${e.message}")
            null
        }
    }


    private suspend fun postJsonObject(payload: JSONObject): JSONObject? =
        withContext(Dispatchers.IO) {
            try {
                val url = URL(BASE_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                OutputStreamWriter(connection.outputStream).use { it.write(payload.toString()) }

                val response = connection.inputStream.bufferedReader().use(BufferedReader::readText)
                return@withContext JSONObject(response)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    suspend fun actualizarReserva(reserva: DetalleReserva): Boolean = withContext(Dispatchers.IO)
    {
        try {
            val payload = JSONObject().apply {
                put("accion", "actualizar")
                put("hoja", "DetalleReserva")
                put("ID", reserva.id)
                put("ClienteID", reserva.clienteId)
                put("NombreCompleto", reserva.nombreCompleto)
                put("NombreUsuario", reserva.nombreUsuario)
                put("Telefono", reserva.telefono)
                put("CodigoTicket", reserva.codigoTicket)
                put("FechaReserva", reserva.fechaReserva)
                put("FechaEntrega", reserva.fechaEntrega)
                put("HoraEntrega", reserva.horaEntrega)
                put("Estado", reserva.estado)
                put("TipoPago", reserva.tipoPago)
                put("Total", reserva.total)
                put("Observaciones", reserva.observaciones)
                put("PuntosGanados", reserva.puntosGanados)
                put("Canjeado", reserva.canjeado)
            }

            val response = postJsonObject(payload)
            val success = response?.optBoolean("success") ?: false
            Log.d("DetalleReserva", "✅ Actualización: $success")
            success
        } catch (e: Exception) {
            Log.e("DetalleReserva", "❌ Error al actualizar reserva: ${e.message}", e)
            false
        }
    }

    suspend fun actualizarReservaMultiple(registros: List<DetalleReserva>): Boolean =
        withContext(Dispatchers.IO)
        {
            try {
                val payload = JSONObject().apply {
                    put("accion", "actualizarMultiple")
                    put("hoja", "DetalleReserva")
                    put("registros", JSONArray().apply {
                        registros.forEach { reserva ->
                            put(JSONObject().apply {
                                put("ID", reserva.id)
                                put("ClienteID", reserva.clienteId)
                                put("NombreCompleto", reserva.nombreCompleto)
                                put("NombreUsuario", reserva.nombreUsuario)
                                put("Telefono", reserva.telefono)
                                put("CodigoTicket", reserva.codigoTicket)
                                put("FechaReserva", reserva.fechaReserva)
                                put("FechaEntrega", reserva.fechaEntrega)
                                put("HoraEntrega", reserva.horaEntrega)
                                put("Estado", reserva.estado)
                                put("TipoPago", reserva.tipoPago)
                                put("Total", reserva.total)
                                put("Observaciones", reserva.observaciones)
                                put("PuntosGanados", reserva.puntosGanados)
                                put("Canjeado", reserva.canjeado)
                            })
                        }
                    })
                }

                val response = postJsonObject(payload)
                response?.optBoolean("error") == false
            } catch (e: Exception) {
                Log.e("DetalleReserva", "❌ Error actualizarMultiple: ${e.message}", e)
                false
            }
        }
}







