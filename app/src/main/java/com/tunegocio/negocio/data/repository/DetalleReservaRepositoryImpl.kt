package com.tunegocio.negocio.data.repository


import com.tunegocio.negocio.domain.model.reserva.DetalleReserva
import com.tunegocio.negocio.domain.repository.DetalleReservaRepository

class DetalleReservaRepositoryImpl : DetalleReservaRepository {


    override suspend fun listar(): List<DetalleReserva> {
        TODO("listar() aún no implementado. Usá listarReservasPorClienteYFecha o listarReservasPorClienteYRango si aplica.")
    }

    override suspend fun agregar(detalle: DetalleReserva): Boolean {
        TODO("agregar() aún no implementado. Podés usar registrarCarrito() si aplica.")
    }

    override suspend fun actualizar(detalle: DetalleReserva): Boolean {
        TODO("actualizar() aún no implementado.")
    }

    override suspend fun eliminar(id: String): Boolean {
        TODO("eliminar() aún no implementado.")
    }
}
