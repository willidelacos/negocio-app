// UsuarioUseCases.kt
package com.tunegocio.negocio.domain.usecase

import com.tunegocio.negocio.domain.repository.UsuarioRepository
import com.tunegocio.negocio.domain.usecase.usuario.*

data class UsuarioUseCases(
    val obtenerUsuarios: ObtenerUsuariosUseCase,
    val agregarUsuario: AgregarUsuarioUseCase,
    val actualizarUsuario: ActualizarUsuarioUseCase,
    val eliminarUsuario: EliminarUsuarioUseCase,
) {
    companion object {
        fun provide(repository: UsuarioRepository): UsuarioUseCases {
            return UsuarioUseCases(
                obtenerUsuarios = ObtenerUsuariosUseCase(repository),
                agregarUsuario = AgregarUsuarioUseCase(repository),
                actualizarUsuario = ActualizarUsuarioUseCase(repository),
                eliminarUsuario = EliminarUsuarioUseCase(repository)
            )
        }
    }
}
