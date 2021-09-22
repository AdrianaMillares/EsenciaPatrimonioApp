package com.example.escenciapatrimoniotramites.Modelos

import com.parse.ParseObject
import com.parse.ParseUser

class Seguir : ParseObject() {
    val LLAVE_USUARIO : String = "usuario"
    val LLAVE_TRAMITE : String = "tramite"

    /***************************
     * Setters and Getters
     ***************************/
    var ulrDocumento: ParseUser?
        get() = getParseUser(LLAVE_USUARIO)
        set(urlDocumento) = put(LLAVE_USUARIO, urlDocumento!!)

    var tramite: ParseObject?
        get() = getParseObject(LLAVE_TRAMITE)
        set(tramite) = put(LLAVE_TRAMITE, tramite!!)
}