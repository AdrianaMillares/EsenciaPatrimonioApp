package com.esenciapatrimonio.kampatramites.Activities

import android.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.esenciapatrimonio.kampatramites.Activities.InternetUtils.isNetworkAvailable
import com.esenciapatrimonio.kampatramites.Modelos.Comentar
import com.esenciapatrimonio.kampatramites.Modelos.Tramite
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * Genera la vista donde se ven los trámites y leyes
 * Redirige al usuario a la página donde existe más información sobre el trámite o ley
 * Despliega los comentarios realizados por los usuarios
 * Permite realizar comentarios al usuario
 * @param etComment Elemento de la interfaz donde se muestran los comentarios
 * @param btnPublish Botón de la interfaz que al presionarlo recupera la información y permite subir el comentario
 * @param btnConsultarFormato Botón de la interfaz que redirige al usuario al url del tramite seleccionado
 * @param etTitulo Elemento de la interfaz que contiene el título
 * @param etDescripción Elemento de la interfaz que contiene la descripción
 * @param tramiteActualNombre String que contiene el nombre del tramite actual
 * @param tramiteActualDescripción String que contiene la descripción del tramite actual
 * @param tramiteParseObject Objeto que contiene la información de la tabla [TRAMITES] de la base de datos
 * @param tramiteActualUrl String que contiene la liga que lleva el pdf del tramite actual
 * @param ivArrowInf Elemento de la interfaz que al presionarlo nos lleva a la vista de inicio
 * @param listView Elemento de la interfaz que despliega los comentarios
 * @param list Lista de strings en la que se guardan los comentarios
 * @param adapter Permite enviar información a la interfaz
 * @param btnCompartirInf Botón que al presionarlo da la opción de enviar el tramite/ley interactuando con otras aplicaciones
 * @param nombreTramite Nombre por defecto utilizado para hacer pruebas
 */
class InformationActivity : AppCompatActivity() {

    val TAG = "InformationActivity"
   // private lateinit var etComment: EditText
  //  private lateinit var btnPublish: ImageView
    private lateinit var btnConsultarFormato: Button
    private lateinit var etTitulo: TextView
    private lateinit var etDescripcion: TextView
    private lateinit var tramiteActualNombre: String
    private lateinit var tramiteActualDescripcion: String
    private lateinit var TramiteParseObject: ParseObject
    private lateinit var tramiteActualUrl: String
    private lateinit var ivArrowInf: ImageView
   // private lateinit var listView: ListView
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var btnCompartirInf: TextView
    private var nombreTramite = "Impermeabilizar"
    private lateinit var imagenRnd: String
    private lateinit var ivInfo: ImageView

    /**
     * Se ejecuta al crear la vista, despliega la interfaz
     * Obtiene los datos del trámite/ley actual y los despliega en pantalla
     * Obtiene comentarios y los despliega en pantalla
     * Permite compartir la información interactuando con otras aplicaciones
     * Permite comentar un trámite/ley
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //Verificar la conectividad de internet
        if (isNetworkAvailable(this) == false){
            val i = Intent(this, ConnectionErrorActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }


        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        super.onCreate(savedInstanceState)
         setContentView(com.esenciapatrimonio.kampatramites.R.layout.activity_information)
       // btnPublish = findViewById(com.esenciapatrimonio.kampatramites.R.id.btnComentar)
        btnCompartirInf = findViewById(com.esenciapatrimonio.kampatramites.R.id.btnCompartirInf)
      //  etComment = findViewById(com.esenciapatrimonio.kampatramites.R.id.etComentario)
        etTitulo = findViewById(com.esenciapatrimonio.kampatramites.R.id.tvTituloInf)
        etDescripcion = findViewById(com.esenciapatrimonio.kampatramites.R.id.tvSubtitInf)
        ivArrowInf = findViewById(com.esenciapatrimonio.kampatramites.R.id.ivArrowInf)
        btnConsultarFormato = findViewById(com.esenciapatrimonio.kampatramites.R.id.btnConsultarFormato)
        ivInfo = findViewById(com.esenciapatrimonio.kampatramites.R.id.ivInformation)


        // Obtiene el nombre del trámite/ley previamente seleccionado
        val intent = intent
        nombreTramite = intent.extras?.getString("nombreTramite").toString()
        imagenRnd = intent.extras?.getString("imagenes").toString()

        val imagen= when (imagenRnd.toInt()) {
            1 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite1)
            2 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite2)
            3 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite3)
            4 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite4)
            5 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite5)
            6 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite6)
            7 -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite7)
            else -> (com.esenciapatrimonio.kampatramites.R.drawable.imgtramite1)
        }
        ivInfo.setImageResource(imagen)


        // Petición a la base de datos para encontrar la información
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        query.whereEqualTo("nombre", nombreTramite)
        try {
            val itemList: List<Tramite> = query.find()
            for (tramite in itemList) {
                TramiteParseObject = itemList[0]
                tramiteActualNombre = itemList[0].nombre.toString()
                tramiteActualDescripcion = itemList[0].descripcion.toString()

                tramiteActualUrl = itemList[0].url.toString()
                Log.i(TAG, "nombre $tramiteActualNombre ")
                etTitulo.text = tramiteActualNombre
                etDescripcion.text = tramiteActualDescripcion

                Log.i(TAG, "descripcion $tramiteActualDescripcion ")
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        // Redirige al formato web
        btnConsultarFormato.setOnClickListener {
            val url = tramiteActualUrl
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        // Redirige al inicio
        ivArrowInf.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }

        // Permite compartir interactuando con otras aplicaciones
        btnCompartirInf.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "¡Hola! Revisa la información que encontré sobre $tramiteActualNombre: $tramiteActualDescripcion"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        // Publica los comentarios
    /*    btnPublish.setOnClickListener {
            Log.i(TAG, "onClick login button")
            val comentario = etComment.text.toString()
            val usuario = ParseUser.getCurrentUser().username.toString()
            Log.i(TAG, "username: $usuario comentario: $comentario")
            saveNewComment(comentario, usuario, TramiteParseObject)
        }*/

        list = ArrayList()

        // Obtiene los comentarios de la base de datos
        val query2: ParseQuery<Comentar> = ParseQuery.getQuery(Comentar::class.java)
        query2.whereEqualTo("nombreTramite", nombreTramite)
        try {
            val itemList2: List<Comentar> = query2.find()
            if (itemList2.isEmpty()) {
                list.add("Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? ")
            } else {
                for (comentar in itemList2) {
                    list.add(comentar.idUsario.toString() + ": " + comentar.comentario.toString())
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
/*
        // Despliega los comentarios
        listView = findViewById(com.esenciapatrimonio.kampatramites.R.id.listView)
        val appContext = this
        adapter = ArrayAdapter<String>(appContext, R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }



    /**
     * Inserta los comentarios nuevos en la base de datos
     * Despliega mensajes de error y alerta
     * @param comentario String que contiene el comentario
     * @param usuario String que contiene el nombre del usuario que comentó
     * @param objTramite Objeto que contiene la información del tramite actual
     */
    private fun saveNewComment(
        comentario: String,
        usuario: String,
        objTramite: ParseObject
    ) {
        val usuarioParse = ParseObject.create("Tramite")
        usuarioParse.put("username", usuario)

        val comentar = ParseObject.create("Comentar")
        comentar.put("idUsario", usuario)
        comentar.put("usuario", ParseUser.getCurrentUser())
        comentar.put("comentario", comentario)
        comentar.put("Tramite", objTramite)
        comentar.put("nombreTramite", nombreTramite)
        comentar.saveInBackground { error ->
            if (error == null) {
                Toast.makeText(this, "¡Tu comentario ha sido publicado!", Toast.LENGTH_SHORT).show()
            //    etComment.text.clear()
                list = CommentUtils.commentBoxFormat(list, usuario, comentario)
                adapter.notifyDataSetChanged()
                Log.d("Salida", "Guardado Correctamente")
            }
        }*/
    }
}

object CommentUtils {

    fun commentBoxFormat(list : ArrayList<String>,usuario: String, comentario: String) : ArrayList<String> {

            if (list[0] == "Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? ") {
                list[0] = "$usuario: $comentario"
            } else {
                list.add("$usuario: $comentario")
            }


        return list
    }
}