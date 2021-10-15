package com.example.escenciapatrimoniotramites

import com.example.escenciapatrimoniotramites.Activities.LoginUtils
import com.google.common.truth.Truth.assertThat
<<<<<<< HEAD
import com.parse.ParseException
import junit.framework.TestCase

=======
import org.junit.Test

/**
 * Test [LoginActivity]
 *
 * El input no es valido si ...
 * ... no se ingresó el nombre de usuario o el correo electrónico
 * ... no se ingresó la contraseña
 * ... el nombre de usuario o la contraseña no son correctas
 */
>>>>>>> c6f5ac4377b6446d05a9d93a99d3b54efc13d95e
class LoginUnitTest {
    @Test
    fun `nombre de usuario vacio, se indica que se debe colocar username o password`() {
        val result = LoginUtils.validateLoginError(
            "username/email is required."
        )
        assertThat(result).matches("Se deben ingresar nombre de usuario/email")
    }

    @Test
<<<<<<< HEAD
    fun  `contraseña vacia, se indica que hay que ponerla`  (){
        val result = LoginUtils.validateLoginError (
=======
    fun `contraseña vacia, se indica que hay que ponerla`() {
        val result = LoginUtils.validateLoginError(
>>>>>>> c6f5ac4377b6446d05a9d93a99d3b54efc13d95e
            "password is required."
        )
        assertThat(result).matches("Se debe ingresar la contraseña")
    }

    @Test
<<<<<<< HEAD
    fun  `usuario o contraseña incorrecta, se indica esto`  (){
        val result = LoginUtils.validateLoginError (
=======
    fun `usuario o contraseña incorrecta, se indica esto`() {
        val result = LoginUtils.validateLoginError(
>>>>>>> c6f5ac4377b6446d05a9d93a99d3b54efc13d95e
            "Invalid username/password."
        )
        assertThat(result).matches("El nombre de usuario o la contraseña no son correctos")
    }
<<<<<<< HEAD



}

class resetPasswordTest{
    @Test
    fun  `no hay errores`  (){
        val result = LoginUtils.validateResetPasswordError (
            null
        )
        assertThat(result).matches("Se te envió un correo con las instrucciones")
    }



=======
>>>>>>> c6f5ac4377b6446d05a9d93a99d3b54efc13d95e
}