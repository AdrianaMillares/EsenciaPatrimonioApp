package com.example.escenciapatrimoniotramites

import com.example.escenciapatrimoniotramites.Activities.LoginActivity
import com.example.escenciapatrimoniotramites.Activities.LoginUtils
 import org.junit.Test
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase

class LoginUnitTest {
    @Test
    fun  `nombre de usuario vacio, se indica que se debe colocar username o password`  (){
        val result = LoginUtils.validateLoginError (
            "username/email is required."

        )
        assertThat(result).matches("Se deben ingresar nombre de usuario/email")
    }

    @Test
    fun  ` contraseña vacia, se indica que hay que ponerla   `  (){
        val result = LoginUtils.validateLoginError (
            "password is required."

        )
        assertThat(result).matches("Se debe ingresar la contraseña")
    }

    @Test
    fun  `usuario o contraseña incorrecta, se indica esto `  (){
        val result = LoginUtils.validateLoginError (
            "Invalid username/password."

        )
        assertThat(result).matches("El nombre de usuario o la contraseña no son correctos")
    }



}