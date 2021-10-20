package com.esenciapatrimonio.kampatramites.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Documentos")
class Documentos : ParseObject() {
    val LLAVE_URL_DOCUMENTO : String = "urlDocumento"
    val LLAVE_TRAMITE : String = "tramite"

    /***************************
     * Setters and Getters
     ***************************/
    var ulrDocumento: String?
        get() = getString(LLAVE_URL_DOCUMENTO)
        set(urlDocumento) = put(LLAVE_URL_DOCUMENTO, urlDocumento!!)

    var tramite: ParseObject?
        get() = getParseObject(LLAVE_TRAMITE)
        set(tramite) = put(LLAVE_TRAMITE, tramite!!)
}