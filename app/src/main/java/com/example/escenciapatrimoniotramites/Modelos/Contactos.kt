package com.example.escenciapatrimonioinstitutos.Modelos

import com.parse.ParseObject

class Contactos : ParseObject() {
    val LLAVE_NOMBRE : String = "nombre"
    val LLAVE_INSTITUTO : String = "instituto"

    /***************************
     * Setters and Getters
     ***************************/
    var nombre: String?
        get() = getString(LLAVE_NOMBRE)
        set(nombre) = put(LLAVE_NOMBRE, nombre!!)

    var instituto: ParseObject?
        get() = getParseObject(LLAVE_INSTITUTO)
        set(instituto) = put(LLAVE_INSTITUTO, instituto!!)

}