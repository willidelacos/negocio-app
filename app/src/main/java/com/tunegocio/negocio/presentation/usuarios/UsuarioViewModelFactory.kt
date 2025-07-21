package com.tunegocio.negocio.presentation.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunegocio.negocio.domain.usecase.UsuarioUseCases
import com.tunegocio.negocio.presentation.usuarios.UsuarioViewModel

class UsuarioViewModelFactory(
    private val useCases: UsuarioUseCases
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(useCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}