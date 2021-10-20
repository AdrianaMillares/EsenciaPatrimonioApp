package com.esenciapatrimonio.kampatramites.Activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esenciapatrimonio.kampatramites.Modelos.Tramite
import com.esenciapatrimonio.kampatramites.R
import com.parse.ParseException
import com.parse.ParseQuery

/**
 * Despleiga en pantalla una vista tipo grid con todos los trámites o leyes para tener una mejor visión de estos
 * Obtiene la información de los trámites o leyes de la base de datos
 * @param titulo String que contiene la categoría seleccionada, trámite o ley
 * @param lista Lista de Tramites que contiene todos los trámites existentes en la base de datos
 * @param list Lista de strings que contiene los nombres de los trámites ya existentes en la interfaz para evitar duplicidad
 * @param intent Obtiene la información enviada de la vista [home]
 * @param recyclerView Elemento de la interfaz donde se despliegan los trámites
 * @param tvTituloVerMas Elemento en el que se muestra el título de la categoría seleccionada trámites/leyes
 * @param adapterViewMore Permite desplegar la información en la interfaz
 */
class ViewMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        var arrow: ImageView = findViewById(com.esenciapatrimonio.kampatramites.R.id.ivArrow)

        //verificar conexion a internet
        if (InternetUtils.isNetworkAvailable(this) == false){
            val i = Intent(this, ConnectionErrorActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }

        val titulo: String
        val lista: ArrayList<Tramite> = ArrayList()
        var imagenRnd: ArrayList<Int> = ArrayList()
        val list: ArrayList<String> = ArrayList()
        val intent = getIntent()
        val tvTituloVerMas: TextView = findViewById(R.id.tvTituloVerMas)

        imagenRnd = intent.extras?.getIntegerArrayList("imagenes") as ArrayList<Int>

        // Recupera la categoría seleccionada
        titulo = intent.extras?.getString("categoria").toString();
        val tempBool: Boolean
        if (titulo == "Trámites") {
            tempBool = true
        } else {
            tempBool = false
        }

        // Obtiene la información de la categoría seleccionada
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        query.whereEqualTo("tramite", tempBool)

        try {
            val itemList: List<Tramite> = query.find()
            for (tramite in itemList) {
                // Revisa que el tramite no esté en la lista para evitar duplicidad
                if (tramite.nombre.toString() !in list) {
                    if (tramite.esTramite == true) {
                        lista.add(tramite)
                    } else {
                        lista.add(tramite)
                    }
                    list.add(tramite.nombre.toString())
                    Log.i("tramite", "$tramite.nombre.toString()")
                } else {
                    continue
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Despliega los datos en pantalla
        tvTituloVerMas.text = titulo
        val adapterViewMore: ViewMoreAdapter = ViewMoreAdapter(lista,imagenRnd, this
        ) { position -> onListItemClick(position) }
        val recyclerView: RecyclerView = findViewById(R.id.rvViewMore)
        val manager = GridLayoutManager(this, 2)
        manager.scrollToPosition(0)

        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterViewMore
        recyclerView.itemAnimator = DefaultItemAnimator()

        // Redirige al inicio
        arrow.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }
    }

    private fun onListItemClick(strTramite: String) {}

    /**
     * Redirige a la vista del trámite seleccionado
     */
    fun goInformationActivity(tituloTramite: String) {
        val i = Intent(this, InformationActivity::class.java)
        i.putExtra("titulo", tituloTramite)
        startActivity(i)
    }



}

/**
 * Permite mostrar información en la interfaz
 * @param dataSet Lista de trámites
 * @param contexto Contiene el contexto de la página donde se muestran los trámites/leyes
 * @param onItemClicked Almacena la información del trámite/ley seleccionado
 */
class ViewMoreAdapter(
    private val dataSet: ArrayList<Tramite>,
    private val dataSet2: ArrayList<Int>,
    private val contexto: Context,
    private val onItemClicked: (strTramite: String) -> Unit
) :
    RecyclerView.Adapter<ViewMoreAdapter.ViewHolder>() {
    class ViewHolder(
        view: View,
        private val onItemClicked: (strTramite: String) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView4)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            view.setOnClickListener(this)
        }

        /**
         * Recupera el nombre del trámite/ley seleccionado y envia la informacion a [InformationActivity] para generar
         * la vista del objeto seleccionado
         * @param textView Elemento de la interfaz que contiene el nombre del trámite/ley
         * @param strTramite String que contiene el nombre del trámite
         */
        override fun onClick(view: View) {
            val textView: TextView = view.findViewById(R.id.textView4)
            val strTramite = textView.text.toString()
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val strRnd = imageView.contentDescription
            val intent = Intent(view.context, InformationActivity::class.java)
            intent.putExtra("nombreTramite", strTramite)
            intent.putExtra("imagenes",strRnd)

            (view.context as ViewMoreActivity?)!!.startActivity(intent)
            onItemClicked(strTramite)
        }

        fun bind(str: String, contexto: Context) {
            textView.text = str
        }
    }

    /**
     * Crea las nuevas vistas con los elementos seleccionados en forma de grid
     * @return los elementos con los datos
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        Log.i("oncreateviewholder", "a punto de hacer return")

        return ViewHolder(
            layoutInflater.inflate(R.layout.text_row_item, viewGroup, false),
            onItemClicked
        )
    }

    /**
     * Despliega la información de la base de datos en la interfaz
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.i(
            "onbindviewholder",
            "a punto de obtener dataset y wardarlo en viewholder.textview.text"
        )

        val imagenRnd = when (dataSet2[position]) {
            1 -> (R.drawable.imgtramite1)
            2 -> (R.drawable.imgtramite2)
            3 -> (R.drawable.imgtramite3)
            4 -> (R.drawable.imgtramite4)
            5 -> (R.drawable.imgtramite5)
            6 -> (R.drawable.imgtramite6)
            7 -> (R.drawable.imgtramite7)
            else -> (R.drawable.imgtramite1)
        }
        viewHolder.imageView.setImageResource(imagenRnd)
        viewHolder.imageView.contentDescription = dataSet2[position].toString()
        viewHolder.bind(dataSet[position].nombre.toString(), contexto)
        viewHolder.textView.text = dataSet[position].nombre.toString()
    }

    override fun getItemCount() = dataSet.size
}