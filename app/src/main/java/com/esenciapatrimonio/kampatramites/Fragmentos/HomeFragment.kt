package com.esenciapatrimonio.kampatramites.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esenciapatrimonio.kampatramites.Activities.*
import com.esenciapatrimonio.kampatramites.Modelos.Tramite
import com.esenciapatrimonio.kampatramites.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * @param listaTramites Lista de [Trámites] que almacena la información de los tramites para evitar duplicidad
 * @param listaLeyes Lista de [Leyes] que almacena la información de los tramites para evitar duplicidad
 */
val listaTramites: ArrayList<Tramite> = ArrayList()
val listaLeyes: ArrayList<Tramite> = ArrayList()
val imgTramite: ArrayList<Int> = ArrayList()
val imgLey: ArrayList<Int> = ArrayList()

/**
 * Despliega la página de inicio donde se puede ver una vista de leyes y tramites
 * Permite cerrar sesión
 * Obtiene la información de los tramites y leyes y los despleiga en pantalla
 * @param recyclerView1 Elemento de la interfaz donde se despliegan los trámites
 * @param recyclerView2 Elemento de la interfaz donde se despliegan las leyes
 * @param adapterLeyes Permite desplegar la información en la interfaz
 * @param adapterTramites Permite desplegar la información en la interfaz
 * @param tvVerTramites Elemento en el que se muestra el título "Trámites"
 * @param tvVerLeyes Elemento en el que se muestra el título "Leyes"
 * @param btnLogOut Elemento de la interfaz que referencía al botón que permite cerrar sesión
 * @param list Lista que almacena el nombre de los trámites y leyes en uso para evitar duplicidad
 */
class HomeFragment : Fragment() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapterLeyes: CustomAdapter
    private lateinit var adapterTramites: CustomAdapter
    private lateinit var tvVerTramites: TextView
    private lateinit var tvVerLeyes: TextView

    var user: ParseUser = ParseUser.getCurrentUser()
    var politicas: Boolean = user.getBoolean("politicas")

    private var list: ArrayList<String> = ArrayList()
    private val TAG: String = "HomeFragment"

    /**
     * Obtiene la información del fragmento y la despliega en pantalla
     * Permite cerrar sesión redirigiendo a la vista de [LogIn]
     * @param vista Almacena la información del fragmento
     * @return vista
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView1 = vista.findViewById(R.id.recyclerView1)
        recyclerView2 = vista.findViewById(R.id.recyclerView2)
        tvVerLeyes = vista.findViewById(R.id.tvVerLeyes)
        tvVerTramites = vista.findViewById(R.id.tvVerTramites)

        val tempLayoutMan = LinearLayoutManager(activity)
        Log.i("rv", recyclerView1.toString())
        Log.i("context", activity.toString())
        Log.i("templayoutmanager", tempLayoutMan.toString())
        return vista
    }

    /**
     * Se ejecuta una vez que la vista se encuentre creada
     * Permite el Log Out
     * Obtiene la informacion de [Tramite] y la despliega en pantalla
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!politicas) {
            val condiciones = View.inflate(context, R.layout.condiciones, null)
            val check: CheckBox = condiciones.findViewById(R.id.checkBox)
            val btnContinuar: Button = condiciones.findViewById(R.id.button)
            val builder = MaterialAlertDialogBuilder(requireContext()).setView(condiciones)
            val dialog = builder.create()
            dialog.show()

            condiciones.isFocusable = true
            btnContinuar.isEnabled = false

            check.setOnCheckedChangeListener { _, isChecked ->
                btnContinuar.isEnabled = isChecked
            }

            btnContinuar.setOnClickListener {
                dialog.dismiss()
                user.put("politicas",true)
                user.save()
            }
        }

        adapterTramites = CustomAdapter(listaTramites,imgTramite, requireContext()
        ) { position -> onListItemClick(position) }
        adapterLeyes = CustomAdapter(listaLeyes,imgLey, requireContext()
        ) { position -> onListItemClick(position) }

        // Se realiza una consulta a la base de datos para conseguir la información de [Tramite]
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        if (listaTramites.size == 0 || listaLeyes.size == 0) {
            try {
                val itemList: List<Tramite> = query.find()
                for (tramite in itemList) {

                    // Revisa que el tramite no esté en la lista
                    if (tramite.nombre.toString() !in list) {
                        if (tramite.esTramite == true) {
                            listaTramites.add(tramite)
                            imgTramite.add(randomImg())
                        } else {
                            listaLeyes.add(tramite)
                            imgLey.add(randomImg())
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
        }

        recyclerView1 = view.findViewById(R.id.recyclerView1)
        recyclerView2 = view.findViewById(R.id.recyclerView2)

        val manager = LinearLayoutManager(requireContext())
        val manager2 = LinearLayoutManager(requireContext())

        manager.orientation = LinearLayoutManager.HORIZONTAL
        manager.scrollToPosition(0)

        manager2.orientation = LinearLayoutManager.HORIZONTAL
        manager2.scrollToPosition(0)

        recyclerView1.layoutManager = manager
        recyclerView1.setHasFixedSize(true)
        recyclerView1.adapter = adapterTramites
        recyclerView1.itemAnimator = DefaultItemAnimator()

        recyclerView2.layoutManager = manager2
        recyclerView2.setHasFixedSize(true)
        recyclerView2.adapter = adapterLeyes
        recyclerView2.itemAnimator = DefaultItemAnimator()

        // Al presionar ver todos redirige a [ViewMoreActivity]
        tvVerLeyes.setOnClickListener {
            val newActivity = Intent(context, ViewMoreActivity::class.java)
            newActivity.putExtra("categoria", "Leyes")
            newActivity.putExtra("lista", listaLeyes)
            newActivity.putExtra("imagenes", imgLey)
            (context as MainActivity?)!!.startActivity(newActivity)
        }

        tvVerTramites.setOnClickListener {
            val newActivity = Intent(context, ViewMoreActivity::class.java)
            newActivity.putExtra("categoria", "Trámites")
            newActivity.putExtra("lista", listaTramites)
            newActivity.putExtra("imagenes", imgTramite)
            (context as MainActivity?)!!.startActivity(newActivity)
        }
    }

    private fun randomImg(): Int {
        return (1..7).random()
    }

    private fun onListItemClick(strTramite: String) {}
}

/**
 * Permite mostrar información en la interfaz
 * @param dataSet Lista de trámites
 * @param contexto Contiene el contexto de la página donde se muestran los trámites/leyes
 * @param onItemClicked Almacena la información del trámite/ley seleccionado
 */
class CustomAdapter(
    private val dataSet: ArrayList<Tramite>,
    private val dataSet2: ArrayList<Int>,
    private val contexto: Context,
    private val onItemClicked: (strTramite: String) -> Unit
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(
        view: View,
        private val onItemClicked: (strTramite: String) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView4)
        var imageView: ImageView = view.findViewById(R.id.imageView)


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
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val strTramite = textView.text.toString()
            val strRnd = imageView.contentDescription
            val intent = Intent(view.context, InformationActivity::class.java)
            intent.putExtra("nombreTramite", strTramite)
            intent.putExtra("imagenes",strRnd)
            (view.context as MainActivity?)!!.startActivity(intent)
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