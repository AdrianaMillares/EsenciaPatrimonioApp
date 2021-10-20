package com.esenciapatrimonio.kampatramites

import com.esenciapatrimonio.kampatramites.Activities.RegisterUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Test [RegisterActivity]
 * El input es valido si ...
 * ... Todos los datos cumplen las especificaciones
 *
 * El input no es valido si ...
 * ... no se ingresó el nombre de usuario o el correo electrónico
 * ... no se ingresó la contraseña
 * ... el nombre de usuario o la contraseña no son correctas
 */
class RegisterUnitTest {
    @Test
    fun `registroExitoso`() {
        val result = RegisterUtils.validarInputs(
            "usrnameCorrecto",
            ".PassoWord123",
            ".PassoWord123",
            "correoweno@gmail.com"
        )
        assertThat(result).matches("confirm")
    }

    @Test
    fun `contraseña insegura`() {
        val result = RegisterUtils.validarInputs(
            "usrnameCorrecto",
            ".Passo",
            ".Passo",
            "correoweno@gmail.com"
        )
        assertThat(result).matches("La contraseña debe de contener al menos 8 caracteres, y al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial")
    }

    @Test
    fun `correo no valido`() {
        val result = RegisterUtils.validarInputs(
            "usrnameCorrecto",
            ".PassoWord123",
            ".PassoWord123",
            "correomalo"
        )
        assertThat(result).matches("Debes de ingresar un correo valido")
    }

    @Test
    fun `Las contraseñas deben de ser iguales`() {
        val result = RegisterUtils.validarInputs(
            "usrnameCorrecto",
            ".PassoWord123",
            ".otraPasswordO.o",
            "correoweno@gmail.com"
        )
        assertThat(result).matches("Las contraseñas deben de ser iguales")
    }

    @Test
    fun `no estan todos los campos`() {
        val result = RegisterUtils.validarInputs(
            "",
            "",
            "",
            ""
        )
        assertThat(result).matches("Debes de llenar todos los campos")
    }
}

