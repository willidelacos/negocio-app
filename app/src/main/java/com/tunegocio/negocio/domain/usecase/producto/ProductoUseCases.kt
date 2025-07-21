package com.tunegocio.negocio.domain.usecase.producto

data class ProductoUseCases(
    val obtenerProductos: ObtenerProductosUseCase,
    val agregarProducto: AgregarProductoUseCase,
    val actualizarProducto: ActualizarProductoUseCase,
    val eliminarProducto: EliminarProductoUseCase
) {
    companion object {
        fun provide(repository: com.tunegocio.negocio.domain.repository.ProductoRepository): ProductoUseCases {
            return ProductoUseCases(
                obtenerProductos = ObtenerProductosUseCase(repository),
                agregarProducto = AgregarProductoUseCase(repository),
                actualizarProducto = ActualizarProductoUseCase(repository),
                eliminarProducto = EliminarProductoUseCase(repository)
            )
        }
    }
}
