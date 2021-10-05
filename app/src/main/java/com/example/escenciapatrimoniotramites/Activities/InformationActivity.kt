package com.example.escenciapatrimoniotramites.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.example.escenciapatrimoniotramites.R
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.ParseClassName
import android.widget.Toast

import com.parse.FindCallback
import org.w3c.dom.Text


class InformationActivity : AppCompatActivity() {

    val TAG = "InformationActivity"
     lateinit var etComment: EditText
    lateinit var btnPublish: Button
    lateinit var etTitulo: TextView
    lateinit var etDescripcion: TextView
     var nombreTramite = "Impermeabilizar"
     lateinit var tramiteActualNombre: String
      lateinit var tramiteActualDescripcion :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        btnPublish = findViewById(R.id.btnComentar)
        etComment = findViewById(R.id.etComentario)
        etTitulo = findViewById(R.id.tvTituloInf)
        etDescripcion = findViewById(R.id.tvSubtitInf)
        val tramiteTemp = ParseObject.create("Tramite")
        //ParseQuery <Tramite> query = ParseQuery.getQuery(Tramite.class);

    //   ParseObject.registerSubclass(Tramite.class)
        // Define the class we would like to query
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
// Define our query conditions
        query.whereEqualTo("nombre", nombreTramite)
// Execute the find asynchronously
        Log.d(TAG, "realizando query en bg")
        query.findInBackground { itemList, e ->
            if (e == null) {
                // Access the array of results here
                 tramiteActualNombre = itemList[0].nombre.toString() ;
                 tramiteActualDescripcion = itemList[0].descripcion.toString();
                Log.i(TAG ,"nombre $tramiteActualNombre ");
                etTitulo.text = tramiteActualNombre
                etDescripcion.text = tramiteActualDescripcion
                Log.i(TAG ,"descripcion $tramiteActualDescripcion ");

            } else {
                Log.d("item", "Error: " )
            }


        }
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




    fun saveNewComment(comentario:String, usuario:String, idTramite:String, idUsuario:String  ) {
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