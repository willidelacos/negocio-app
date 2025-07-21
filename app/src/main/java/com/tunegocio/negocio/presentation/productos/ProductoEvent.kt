
package com.tunegocio.negocio.presentation.productos

import com.tunegocio.negocio.domain.model.Producto

sealed class ProductoEvent {
    object CargarProductos : ProductoEvent()
    data class AgregarProducto(val producto: Producto) : ProductoEvent()
    data class ActualizarProducto(val producto: Producto) : ProductoEvent()
    data class EliminarProducto(val id: String) : ProductoEvent()
}
