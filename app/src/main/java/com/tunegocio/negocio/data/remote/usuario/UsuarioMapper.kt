package com.tunegocio.negocio.data.remote.usuario

import com.tunegocio.negocio.domain.model.Usuario
import org.json.JSONObject

object UsuarioMapper {
    fun fromJson(obj: JSONObject): Usuario {
        return Usuario(
            id = obj.optString("ID", ""),
            nombreUsuario = obj.optString("NombreUsuario", ""),
            contrasena = obj.optString("Contraseña", obj.optString("contrasena", obj.optString("contraseña", ""))),
            rol = obj.optString("Rol", ""),
            nombreCompleto = obj.optString("NombreCompleto", ""),
            telefono = obj.optString("Telefono", ""),
            direccion = obj.optString("Direccion", ""),
            estado = obj.optString("Estado", ""),
            fechaNacimiento = obj.optString("FechaNacimiento", ""),
            fechaRegistro = obj.optString("FechaRegistro", "")
        )
    }

    fun toParams(usuario: Usuario): Map<String, String> {
        return mapOf(
            "ID" to usuario.id,
            "NombreUsuario" to usuario.nombreUsuario,
            "Contraseña" to usuario.contrasena,
            "Rol" to usuario.rol,
            "NombreCompleto" to usuario.nombreCompleto,
            "Telefono" to usuario.telefono,
            "Direccion" to usuario.direccion,
            "Estado" to usuario.estado,
            "FechaNacimiento" to usuario.fechaNacimiento,
            "FechaRegistro" to usuario.fechaRegistro
        )
    }
}
