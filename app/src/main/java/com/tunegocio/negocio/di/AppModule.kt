

/**
 * Módulo de provisión de dependencias manual.
 *
 * Este objeto crea y proporciona instancias de repositorios y casos de uso
 * necesarios en la aplicación, evitando el uso de frameworks de inyección de dependencias.
 *
 * Actualmente provee:
 * - LoginRepository: implementación para el login.
 * - LoginUseCase: caso de uso que encapsula la lógica del login.
 *
 * Para añadir nuevas dependencias, ampliar este objeto.
 *
 * Fecha: 2025-07-08
 * Autor: Williams
 */
// Esto se usará de forma manual (por ejemplo, desde una clase ServiceLocator o Application)
package com.tunegocio.negocio.di

import com.tunegocio.negocio.data.repository.LoginRepositoryImpl
import com.tunegocio.negocio.domain.repository.LoginRepository
import com.tunegocio.negocio.domain.usecase.LoginUseCase
import com.tunegocio.negocio.data.repository.UsuarioRepositoryImpl
import com.tunegocio.negocio.domain.repository.UsuarioRepository
import com.tunegocio.negocio.domain.usecase.UsuarioUseCases
import com.tunegocio.negocio.domain.usecase.usuario.ActualizarUsuarioUseCase
import com.tunegocio.negocio.domain.usecase.usuario.AgregarUsuarioUseCase
import com.tunegocio.negocio.domain.usecase.usuario.ObtenerUsuariosUseCase
import com.tunegocio.negocio.domain.usecase.usuario.EliminarUsuarioUseCase

import com.tunegocio.negocio.presentation.usuarios.UsuarioViewModelFactory

object AppModule {

    fun provideLoginRepository(): LoginRepository {
        return LoginRepositoryImpl()
    }

    fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase(provideLoginRepository())

    }
    // Usuarios
    fun provideUsuarioRepository(): UsuarioRepository {
        return UsuarioRepositoryImpl()
    }

    fun provideUsuarioUseCases(): UsuarioUseCases {
        val repo = provideUsuarioRepository()
        return UsuarioUseCases(
            agregarUsuario = AgregarUsuarioUseCase(repo),
            actualizarUsuario = ActualizarUsuarioUseCase(repo),
            eliminarUsuario = EliminarUsuarioUseCase(repo),
            obtenerUsuarios = ObtenerUsuariosUseCase(repo)
        )
    }


    // Proveedor de ViewModel para usuarios si quieres
    fun provideUsuarioViewModelFactory(): UsuarioViewModelFactory {
        return UsuarioViewModelFactory(provideUsuarioUseCases())
    }
}