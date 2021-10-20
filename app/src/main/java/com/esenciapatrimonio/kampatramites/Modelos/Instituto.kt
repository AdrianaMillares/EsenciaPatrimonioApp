package com.esenciapatrimonio.kampatramites.Modelos

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Instituto")
class Instituto : ParseObject() {
    val LLAVE_NOMBRE : String = "nombre"
    val LLAVE_DIRECCION : String = "instituto"
    val LLAVE_TELEFONO : String = "numTelefonico"
    val LLAVE_HORARIO : String = "horario"

    /***************************
     * Setters and Getters
     ***************************/
    var nombre: String?
        get() = getString(LLAVE_NOMBRE)
        set(nombre) = put(LLAVE_NOMBRE, nombre!!)

    var direccion: String?
        get() = getString(LLAVE_DIRECCION)
        set(direccion) = put(LLAVE_DIRECCION, direccion!!)

    var numTelefonico: String?
        get() = getString(LLAVE_TELEFONO)
        set(telefono) = put(LLAVE_TELEFONO, telefono!!)

    var horario: String?
        get() = getString(LLAVE_HORARIO)
        set(horario) = put(LLAVE_HORARIO, horario!!)
}
