package com.example.escenciapatrimoniotramites

import com.example.escenciapatrimoniotramites.Activities.CommentUtils
import com.example.escenciapatrimoniotramites.Activities.RegisterUtils
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
class CommentUnitTest {
    @Test
    fun `este es el primer comentario, se debe remover el texto que invita a comentar`() {
        val list: ArrayList<String> = ArrayList()
        list.add("Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? ")

        val result = CommentUtils.commentBoxFormat(
            list,
            "cut",
            "excelente info! gracias!"
        )
        assertThat(result[0]).doesNotMatch("Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? ")
    }

    @Test
    fun `ya habia mas comentarios, solo se agrega a la lista`() {
        val list: ArrayList<String> = ArrayList()
        list.add("cut: este es un comentario ya existente")

        val result = CommentUtils.commentBoxFormat(
            list,
            "cut",
            "excelente info! gracias!"
        )
        assertThat(result.last()).matches("cut: excelente info! gracias!")
    }


}

