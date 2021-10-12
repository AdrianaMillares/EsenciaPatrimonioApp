package com.example.escenciapatrimonioinstitutos.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Contactos")
class Contactos : ParseObject() {
    val LLAVE_MUNICIPIO : String = "municipio"
    val LLAVE_URL : String = "url"
    val LLAVE_EMAIL : String = "mail"
    val LLAVE_TELEFONO : String = "telefono"


    /***************************
     * Setters and Getters
     ***************************/
    var municipio: String?
        get() = getString(LLAVE_MUNICIPIO)
        set(municipio) = put(LLAVE_MUNICIPIO, municipio!!)

    var url: String?
        get() = getString(LLAVE_URL)
        set(url) = put(LLAVE_URL, url!!)

    var email: String?
        get() = getString(LLAVE_EMAIL)
        set(email) = put(LLAVE_EMAIL, email!!)

    var telefono: String?
        get() = getString(LLAVE_TELEFONO)
        set(telefono) = put(LLAVE_TELEFONO, telefono!!)


}