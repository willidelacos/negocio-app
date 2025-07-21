package com.tunegocio.negocio.data.remote.detalle_reserva
import org.json.JSONObject
import com.tunegocio.negocio.domain.model.reserva.DetalleReserva

object DetalleReservaMapper {

    fun fromJson(obj: JSONObject): DetalleReserva {
        return DetalleReserva(
            id = obj.optString("ID", obj.optString("id", "")),
            codigoTicket = obj.optString("CodigoTicket", obj.optString("codigoTicket", "")),
            clienteId = obj.optString("ClienteID", obj.optString("clienteId", "")),
            nombreCompleto = obj.optString("NombreCompleto", ""),
            nombreUsuario = obj.optString("NombreUsuario", ""),
            telefono = obj.optString("Telefono", ""),
            fechaReserva = obj.optString("FechaReserva", ""),
            fechaEntrega = obj.optString("FechaEntrega", ""),
            horaEntrega = obj.optString("HoraEntrega", ""),
            estado = obj.optString("Estado", ""),
            tipoPago = obj.optString("TipoPago", ""),
            total = obj.optString("Total", "0.0").replace(",", ".").toDoubleOrNull() ?: 0.0,
            observaciones = obj.optString("Observaciones", ""),
            puntosGanados = obj.optString("PuntosGanados", "0").toIntOrNull() ?: 0,
            canjeado = obj.optString("Canjeado", "NO"),

            // ✅ nuevos campos añadidos
            productoId = obj.optString("ProductoID", obj.optString("productoId", "")),
            nombreProducto = obj.optString("NombreProducto", ""),
            cantidad = obj.optInt("Cantidad", 0),
            subtotal = obj.optString("Subtotal", "0.0").replace(",", ".").toDoubleOrNull() ?: 0.0
        )
    }

    fun toParams(reserva: DetalleReserva): Map<String, String> {
        return mapOf(
            "ID" to reserva.id,
            "CodigoTicket" to reserva.codigoTicket,
            "ClienteID" to reserva.clienteId,
            "NombreCompleto" to reserva.nombreCompleto,
            "NombreUsuario" to reserva.nombreUsuario,
            "Telefono" to reserva.telefono,
            "FechaReserva" to reserva.fechaReserva,
            "FechaEntrega" to reserva.fechaEntrega,
            "HoraEntrega" to reserva.horaEntrega,
            "Estado" to reserva.estado,
            "TipoPago" to reserva.tipoPago,
            "Total" to reserva.total.toString(),
            "Observaciones" to reserva.observaciones,
            "PuntosGanados" to reserva.puntosGanados.toString(),
            "ProductoID" to reserva.productoId,
            "NombreProducto" to reserva.nombreProducto,
            "Cantidad" to reserva.cantidad.toString(),
            "Subtotal" to reserva.subtotal.toString(),
            "Canjeado" to reserva.canjeado.toString()  // ✅ Esta línea era el error probable
        )
    }


}
