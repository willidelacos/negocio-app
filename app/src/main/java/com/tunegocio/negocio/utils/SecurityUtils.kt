/**
 * Genera un hash SHA-256 de la contraseña concatenada con un salt.
 * Luego codifica el resultado en Base64 para obtener un string legible.
 *
 * @param password Contraseña en texto plano
 * @param salt Cadena para saltear la contraseña (por defecto "FLORA")
 * @return String codificado en Base64 del hash SHA-256
 */
package com.tunegocio.negocio.utils
import android.util.Base64
import java.security.MessageDigest

object SecurityUtils {

    fun hashPassword(password: String, salt: String = "FLORA"): String {
        // Concatenar la contraseña con el salt
        val passwordWithSalt = password + salt
        // Obtener instancia de SHA-256
        val digest = MessageDigest.getInstance("SHA-256")
        // Obtener instancia de SHA-256
        val hashBytes = digest.digest(passwordWithSalt.toByteArray())
        // Codificar hash en Base64 sin saltos de línea
        return android.util.Base64.encodeToString(hashBytes, android.util.Base64.NO_WRAP)
    }
}