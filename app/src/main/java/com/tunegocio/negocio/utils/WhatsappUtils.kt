package com.tunegocio.negocio.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.tunegocio.negocio.data.local.UserSessionManager
import com.tunegocio.negocio.data.remote.celular.GoogleSheetsService
import com.tunegocio.negocio.domain.model.reserva.ReservaConProductos
import kotlinx.coroutines.*
import java.net.URLEncoder

/**
 * Abre WhatsApp con un mensaje personalizado para el cliente o admin según el rol del usuario.
 *
 * @param context Contexto actual
 * @param reserva Datos de la reserva
 * @param fechaEntrega Fecha de entrega (formato yyyy-MM-dd)
 * @param horaEntrega Hora de entrega (formato HH:mm)
 */
fun abrirWhatsApp(
    context: Context,
    reserva: ReservaConProductos,
    fechaEntrega: String,
    horaEntrega: String
) {
    CoroutineScope(Dispatchers.IO).launch {
        val numero = try {
            GoogleSheetsService.obtenerNumeroWhatsApp()
        } catch (e: Exception) {
            null
        }

        if (numero != null) {
            val usuario = UserSessionManager(context).obtenerUsuarioSesion()
            val rol = usuario?.rol?.lowercase() ?: "cliente"

            val productosTexto = reserva.productos.joinToString("\n") {
                "- ${it.nombreProducto} x${it.cantidad} = Bs ${"%.2f".format(it.subtotal)}"
            }

            val total = reserva.productos.sumOf { it.subtotal }

            val mensaje = if (rol == "admin") {
                """
                *Panadería y Pastelería FLOR*

                Estimado/a *${reserva.nombreCompleto}*, su pedido ya está listo. ✅

                🧾 *Ticket:* ${reserva.codigoTicket}
                📅 *Entrega:* $fechaEntrega - $horaEntrega

                🛒 *Productos:*
                $productosTexto

                💵 *Total:* Bs ${"%.2f".format(total)}

                ¡Gracias por su preferencia! 🥐
                """.trimIndent()
            } else {
                """
                *Panadería y Pastelería FLOR*

                🧾 *Ticket:* ${reserva.codigoTicket}
                👤 *Cliente:* ${reserva.nombreCompleto}
                📞 *Teléfono:* ${reserva.telefono}
                📅 *Fecha entrega:* $fechaEntrega - $horaEntrega

                🛒 *Pedido:*
                $productosTexto

                💵 *Total:* Bs ${"%.2f".format(total)}

                ¡Gracias por comprar con nosotros! 🍞
                """.trimIndent()
            }

            val uri = Uri.parse("https://wa.me/$numero?text=" + URLEncoder.encode(mensaje, "UTF-8"))
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            withContext(Dispatchers.Main) {
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Número de WhatsApp no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
