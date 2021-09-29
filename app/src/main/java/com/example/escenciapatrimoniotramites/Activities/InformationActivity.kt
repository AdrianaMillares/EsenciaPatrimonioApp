package com.example.escenciapatrimoniotramites.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

class InformationActivity : AppCompatActivity() {

    val TAG = "InformtionActivity"
     lateinit var etComment: EditText
    lateinit var btnPublish: Button
    lateinit var etTramiteId: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        btnPublish = findViewById(R.id.btnComentar)
        etComment = findViewById(R.id.etComentario)
        val tramiteTemp = ParseObject.create("Tramite")
        //ParseQuery <Tramite> query = ParseQuery.getQuery(Tramite.class);

        btnPublish.setOnClickListener{
            Log.i(TAG, "onClick login button")
            var comentario = etComment.text.toString()
            var idUsuario = ParseUser.getCurrentUser().toString()
            var usuario = ParseUser.getCurrentUser().username.toString()
            var idTramite ="1" // todo: definir como obtener el idtramitec
            Log.i(TAG, "username: $usuario comentario: $comentario")
            saveNewComment(comentario, usuario, idTramite, idUsuario)
        }

    }




    fun saveNewComment(comentario:String, usuario:String, idTramite:String, idUsuario:String ) {
        val usuarioParse = ParseObject.create("User")
        usuarioParse.put("username", usuario);
        val comentar = ParseObject.create("Comentar")
        comentar.put("idComentario", "id");
        //comentar.put("tramite", idTramite);
        comentar.put("idUsario", idUsuario);
        comentar.put("usuario",ParseUser.getCurrentUser());
        //comentar.put("usuario",usuarioParse)
        comentar.put("comentario", comentario);

        comentar.saveInBackground{error ->
            if (error == null){
                Log.d("Salida","Guardado Correctamente")
            }

        }
    }

}