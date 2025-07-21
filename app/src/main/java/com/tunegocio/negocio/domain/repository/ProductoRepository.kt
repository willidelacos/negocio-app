package com.tunegocio.negocio.domain.repository

import com.tunegocio.negocio.domain.model.Producto

interface ProductoRepository {
    suspend fun listar(): List<Producto>
    suspend fun agregar(producto: Producto): Boolean
    suspend fun actualizar(producto: Producto): Boolean
    suspend fun eliminar(id: String): Boolean
}