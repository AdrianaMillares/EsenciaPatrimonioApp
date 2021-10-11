package com.example.escenciapatrimoniotramites.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import androidx.recyclerview.widget.RecyclerView

import org.w3c.dom.Text
import android.R
import android.content.Intent
import android.net.Uri

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.escenciapatrimoniotramites.Fragmentos.listaLeyes
import com.example.escenciapatrimoniotramites.Fragmentos.listaTramites
import com.example.escenciapatrimoniotramites.Modelos.Comentar
import com.parse.*
import kotlinx.coroutines.CoroutineStart


class InformationActivity : AppCompatActivity() {

    val TAG = "InformationActivity"
     lateinit var etComment: EditText
    lateinit var btnPublish: Button
    lateinit var btnConsultarFormato: Button
    lateinit var etTitulo: TextView
    lateinit var etDescripcion: TextView
     var nombreTramite = "Impermeabilizar"
     lateinit var tramiteActualNombre: String
      lateinit var tramiteActualDescripcion :String
      lateinit var TramiteParseObject: ParseObject
    lateinit var tramiteActualUrl :String
      lateinit var rvComentarios : RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var ivArrowInf : ImageView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>
    lateinit var btnCompartirInf: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.escenciapatrimoniotramites.R.layout.activity_information)
        btnPublish = findViewById(com.example.escenciapatrimoniotramites.R.id.btnComentar)
        btnCompartirInf = findViewById(com.example.escenciapatrimoniotramites.R.id.btnCompartirInf)
        etComment = findViewById(com.example.escenciapatrimoniotramites.R.id.etComentario)
        etTitulo = findViewById(com.example.escenciapatrimoniotramites.R.id.tvTituloInf)
        etDescripcion = findViewById(com.example.escenciapatrimoniotramites.R.id.tvSubtitInf)
        ivArrowInf = findViewById(com.example.escenciapatrimoniotramites.R.id.ivArrowInf)
        btnConsultarFormato = findViewById(com.example.escenciapatrimoniotramites.R.id.btnConsultarFormato)

        val tramiteTemp = ParseObject.create("Tramite")
        val intent = getIntent()
        nombreTramite = intent.extras?.getString("nombreTramite").toString();

       // rvComentarios = findViewById(com.example.escenciapatrimoniotramites.R.id.rvComentarios)
        //ParseQuery <Tramite> query = ParseQuery.getQuery(Tramite.class);

   /*
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
         query.whereEqualTo("nombre", nombreTramite)
         Log.d(TAG, "realizando query en bg")
        query.findInBackground { itemList, e ->
            if (e == null) {
                // Access the array of results here
                    TramiteParseObject = itemList[0];
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
*/
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        query.whereEqualTo("nombre", nombreTramite)

             try {
                val itemList: List<Tramite> = query.find()
                for (tramite in itemList) {
                    TramiteParseObject = itemList[0];
                    tramiteActualNombre = itemList[0].nombre.toString() ;
                    tramiteActualDescripcion = itemList[0].descripcion.toString();

                    tramiteActualUrl = itemList[0].url.toString();
                    Log.i(TAG ,"nombre $tramiteActualNombre ");
                    etTitulo.text = tramiteActualNombre
                    etDescripcion.text = tramiteActualDescripcion

                    Log.i(TAG ,"descripcion $tramiteActualDescripcion ");

                }
                // customAdapter.updateList(list)

            } catch (e: ParseException) {
                e.printStackTrace()
                 Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            //Thread.sleep(2_000)  // wait for 1 second

        btnConsultarFormato.setOnClickListener{

            val url = tramiteActualUrl
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)


        }

        ivArrowInf.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finishAffinity() // Cierra todas las ventanas anteriores
        }

        btnCompartirInf.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "¡Hola! Revisa la información que encontré sobre $tramiteActualNombre: $tramiteActualDescripcion")
                type = "text/plain"

            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }


        btnPublish.setOnClickListener{
            Log.i(TAG, "onClick login button")
            var comentario = etComment.text.toString()
            var idUsuario = ParseUser.getCurrentUser().toString()
            var usuario = ParseUser.getCurrentUser().username.toString()
            var idTramite ="1" // todo: definir como obtener el idtramitec
            Log.i(TAG, "username: $usuario comentario: $comentario")
            saveNewComment(comentario, usuario, idTramite, idUsuario, TramiteParseObject)
        }
        lateinit var comentarioParsed : String
        list = ArrayList()

         var tempUser : ParseUser?
        // Query para obtener los comentariños

        val query2: ParseQuery<Comentar> = ParseQuery.getQuery(Comentar::class.java)

        query2.whereEqualTo("nombreTramite", nombreTramite)

        try {
            val itemList2: List<Comentar> = query2.find()
            if (itemList2.isEmpty()){
                list.add("Comentarios")

                list.add ("Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? ")
            }

            else {
                list.add("Comentarios")

                for (comentar in itemList2) {
                    tempUser = comentar.usuario

                    list.add(comentar.idUsario.toString() + ": " + comentar.comentario.toString())
                }
                // customAdapter.updateList(list)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        /*
        query2.whereEqualTo("nombreTramite", nombreTramite)
        Log.d(TAG, "realizando query en bg")
        query2.findInBackground { itemList2, e ->
            if (e == null) {

                for (comentar in itemList2) {
                        tempUser = comentar.usuario

                        list.add( comentar.idUsario.toString()+ ": "+ comentar.comentario.toString())
                }
                // Access the array of results here



            } else {
                Log.d("item", "Error: " )
            }

        }*/


        listView = findViewById(com.example.escenciapatrimoniotramites.R.id.listView)
        val appContext = this
        adapter = ArrayAdapter<String>(appContext, R.layout.simple_list_item_1, list)
        listView.adapter = adapter

    }

     fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        listView = findViewById(com.example.escenciapatrimoniotramites.R.id.listView)
        list = ArrayList()

        val appContext = this
        adapter = ArrayAdapter<String>(appContext, R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }


    fun saveNewComment(comentario:String, usuario:String, idTramite:String, idUsuario:String, objTramite:ParseObject  ) {
        val usuarioParse = ParseObject.create("Tramite")
        usuarioParse.put("username", usuario);
        val comentar = ParseObject.create("Comentar")
         //comentar.put("tramite", idTramite);
        comentar.put("idUsario", usuario);
        comentar.put("usuario",ParseUser.getCurrentUser());
        //comentar.put("usuario",usuarioParse)
        comentar.put("comentario", comentario);
        comentar.put("Tramite", objTramite);
        comentar.put("nombreTramite", nombreTramite)
        comentar.saveInBackground{error ->
            if (error == null){
                Toast.makeText(this, "¡Tu comentario ha sido publicado!", Toast.LENGTH_SHORT).show()
                etComment.text.clear()
                if (list[1] == "Aún no hay comentarios. ¡Sé la primera persona en comentar! ¿Te fue útil la información? "){
                    list[1] = "$usuario: $comentario"
                }
                else {
                    list.add("$usuario: $comentario")
                }
                adapter.notifyDataSetChanged()
                Log.d("Salida","Guardado Correctamente")

            }

        }

    }

}
/*
class CustomAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(com.example.escenciapatrimoniotramites.R.id.tvTituloInf)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(com.example.escenciapatrimoniotramites.R.layout.activity_information, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}*/