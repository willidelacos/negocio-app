

package com.tunegocio.negocio.util

fun transformarLinkDrive(link: String): String {
    val regex = Regex("""/d/([a-zA-Z0-9_-]+)""")
    val match = regex.find(link)
    val fileId = match?.groups?.get(1)?.value
    return if (fileId != null) {
        "https://drive.google.com/uc?export=download&id=$fileId"
    } else {
        link
    }
}
