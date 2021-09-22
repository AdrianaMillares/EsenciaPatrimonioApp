package com.example.escenciapatrimoniotramites.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Tramite")
class Tramite : ParseObject() {
    val LLAVE_NOMBRE : String = "nombre"
    val LLAVE_DESCRIPCION : String = "descripcion"

    /***************************
     * Setters and Getters
     ***************************/
    var nombre: String?
        get() = getString(LLAVE_NOMBRE)
        set(nombre) = put(LLAVE_NOMBRE, nombre!!)

    var descripcion: String?
        get() = getString(LLAVE_DESCRIPCION)
        set(descripcion) = put(LLAVE_DESCRIPCION, descripcion!!)
}