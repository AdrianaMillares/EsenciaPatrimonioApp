package com.example.escenciapatrimoniotramites

import com.example.escenciapatrimoniotramites.Activities.CommentUtils
import com.example.escenciapatrimoniotramites.Activities.RegisterUtils
import com.example.escenciapatrimoniotramites.Fragmentos.ProfileUtils
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
class ProfileUnitTest {
    @Test
    fun `los password son iguales, da true`() {

        val result = ProfileUtils.verificarCoincidenciaPasswords(
            "PssWord2#",
            "PssWord2#"
        )
        assertThat(result).isTrue()
    }


    @Test
    fun `los passwords son diferentes, da false`() {

        val result = ProfileUtils.verificarCoincidenciaPasswords(
            "PssWord2#",
            "PssWord"
        )
        assertThat(result).isFalse()
    }

}

