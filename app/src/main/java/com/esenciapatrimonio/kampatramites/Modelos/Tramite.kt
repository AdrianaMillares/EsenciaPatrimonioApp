package com.esenciapatrimonio.kampatramites.Modelos

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseObject
import kotlinx.parcelize.Parcelize

@ParseClassName("Tramite")
@Parcelize
class Tramite  : ParseObject(), Parcelable {
    val LLAVE_NOMBRE : String = "nombre"
    val LLAVE_DESCRIPCION : String = "descripcion"
    val LLAVE_TRAMITE : String ="tramite"
    val LLAVE_URL :String = "url"
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

    var url : String?
        get() = getString(LLAVE_URL)
        set(url) = put(LLAVE_URL, url!!)
}