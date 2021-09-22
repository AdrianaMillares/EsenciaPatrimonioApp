package com.example.escenciapatrimoniotramites.Modelos

import com.parse.Parse
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Comentar")
class Comentar : ParseObject() {
    val LLAVE_COMENTARIO : String = "comentario"
    val LLAVE_TRAMITE : String = "tramite"
    val LLAVE_USUARIO : String = "usuario"

    /***************************
     * Setters and Getters
     ***************************/
    var comentario: String?
        get() = getString(LLAVE_COMENTARIO)
        set(comentario) = put(LLAVE_COMENTARIO, comentario!!)

    var tramite: ParseObject?
        get() = getParseObject(LLAVE_TRAMITE)
        set(tramite) = put(LLAVE_TRAMITE, tramite!!)

    var usuario: ParseUser?
        get() = getParseUser(LLAVE_USUARIO)
        set(usuario) = put(LLAVE_USUARIO, usuario!!)
}