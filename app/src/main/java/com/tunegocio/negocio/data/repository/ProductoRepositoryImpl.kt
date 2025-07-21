package com.tunegocio.negocio.data.repository

import com.tunegocio.negocio.data.remote.producto.ProductoRemoteDataSource
import com.tunegocio.negocio.domain.model.Producto
import com.tunegocio.negocio.domain.repository.ProductoRepository

class ProductoRepositoryImpl : ProductoRepository {
    override suspend fun listar(): List<Producto> {
        return ProductoRemoteDataSource.listarProductos()
    }

    override suspend fun agregar(producto: Producto): Boolean {
        return ProductoRemoteDataSource.agregarProducto(producto)
    }

    override suspend fun actualizar(producto: Producto): Boolean {
        return ProductoRemoteDataSource.actualizarProducto(producto)
    }

    override suspend fun eliminar(id: String): Boolean {
        return ProductoRemoteDataSource.eliminarProducto(id)
    }
}
