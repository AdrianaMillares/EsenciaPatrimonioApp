package com.example.escenciapatrimoniotramites.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Rol")
class Rol : ParseObject() {
    val LLAVE_ROL : String = "nombreRol"

    /***************************
     * Setters and Getters
     ***************************/
    var nombreRol: String?
        get() = getString(LLAVE_ROL)
        set(nombreRol) = put(LLAVE_ROL, nombreRol!!)
}