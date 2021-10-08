package com.example.escenciapatrimoniotramites.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Tramite")
class Tramite : ParseObject() {
    val LLAVE_NOMBRE : String = "nombre"
    val LLAVE_DESCRIPCION : String = "descripcion"
    val LLAVE_TRAMITE : String ="tramite"

    /***************************
     * Setters and Getters
     ***************************/
    var nombre: String?
        get() = getString(LLAVE_NOMBRE)
        set(nombre) = put(LLAVE_NOMBRE, nombre!!)

    var descripcion: String?
        get() = getString(LLAVE_DESCRIPCION)
        set(descripcion) = put(LLAVE_DESCRIPCION, descripcion!!)

    var esTramite : Boolean?
        get() = getBoolean(LLAVE_TRAMITE)
        set(esTramite) = put(LLAVE_TRAMITE, esTramite!!)

}