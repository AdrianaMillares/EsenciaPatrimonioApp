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

/*
*
        with(user) {
            username = nombreusuario
            setPassword(password)
            email = mail


            signUpInBackground { e ->
                if (e == null) {
                    Log.i(TAG, "Al parecer todo salio bien, usuario: $username, correo: $email")
                    goLoginActivity()
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()

                } else {
                    Log.i(TAG, "No se que pudo salio mal :(")
                    Toast.makeText(applicationContext, "Algo falló, vuelve a intentarlo", Toast.LENGTH_SHORT).show()

                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        }
*
* */