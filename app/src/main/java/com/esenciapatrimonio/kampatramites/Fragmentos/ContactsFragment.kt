package com.esenciapatrimonio.kampatramites.Fragmentos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esenciapatrimonio.escenciapatrimonioinstitutos.Modelos.Contactos
import com.esenciapatrimonio.kampatramites.Activities.MainActivity
import com.esenciapatrimonio.kampatramites.R
import com.parse.ParseException
import com.parse.ParseQuery

/**
 * Obtiene y despliega la información del directorio
 * @param recyclerView Elemento de la interfaz que muestra el directorio
 * @param adapter Permite desplegar la información en la interfaz
 * @param list Lista de string que contiene el nombre del municipio para evitar duplicidad
 * @param listaContactos Lista de contactos donde se guarda la información de la base de datos
 */
class ContactsFragment : Fragment() {
    private lateinit var recyclerView1: RecyclerView
    private lateinit var adapter: ContactsAdapter
    private var list: ArrayList<String> = ArrayList()
    private var listaContactos: ArrayList<Contactos> = ArrayList()
    private lateinit var inahUrl: TextView
    /**
     * @param vista Contiene la interfaz de [ContactsFragment]
     * @return la vista de la interfaz
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_contacts, container, false)
        recyclerView1 = vista.findViewById(R.id.rvContactos)
        return vista
    }

    /**
     * Se ejecuta al crear la vista
     * Obtiene y despliega la información de los contactos
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter(
            listaContactos, requireContext()
        )

        val query: ParseQuery<Contactos> = ParseQuery.getQuery(Contactos::class.java)
        try {
            val itemList: List<Contactos> = query.find()
            for (contacto in itemList) {

                // Revisa que el tramite no esté en la lista
                if (contacto.municipio.toString() !in list ) {
                    listaContactos.add(contacto)
                    list.add(contacto.municipio.toString())
                    Log.i("query", contacto.municipio.toString())

                } else {
                    continue
                }
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        // Desplegar el contenido en pantalla
        recyclerView1 = view.findViewById(R.id.rvContactos)
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.scrollToPosition(0)
        recyclerView1.layoutManager = manager
        recyclerView1.setHasFixedSize(true)
        recyclerView1.adapter = adapter
        recyclerView1.itemAnimator = DefaultItemAnimator()
        inahUrl = view.findViewById(R.id.tvInahUrl)


        inahUrl.setText("https://cnci.inah.gob.mx/publico/index.php")
        inahUrl.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://cnci.inah.gob.mx/publico/index.php")
            (context as MainActivity?)!!.startActivity(i)
        }


    }
}

/**
 * Permite mostrar información en la interfaz
 * @param dataSet Lista de trámites
 * @param contexto Contiene el contexto de la página donde se muestran los trámites/leyes
 * @param onItemClicked Almacena la información del trámite/ley seleccionado
 * @param tvMunicipio Elemento del layout donde se muestra el nombre del municipio de contacto
 * @param tvUrl Elemento del layout donde se muestra el url de la página del instituto de contacto
 * @param tvEmail Elemento del layout donde se muestra el correo electrónico de contacto
 * @param tvTelefono Elemento del layout donde se muestra el teléfono del contacto
 */
class ContactsAdapter(private val dataSet: ArrayList<Contactos>, private val contexto: Context) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val tvMunicipio: TextView = view.findViewById(R.id.tvMunicipio)
        private val tvUrl: TextView = view.findViewById(R.id.tvUrl)
        private val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        private val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)

        init {
            view.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            // todo: implementar algo aquí
        }


        fun bind(
            municipio: String,
            url: String,
            email: String,
            telefono: String,
            contexto: Context
        ) {
            tvMunicipio.text = municipio
            tvUrl.text = url
            tvUrl.linksClickable = true

            if (email == "null"){
                tvEmail.text = " "
            }
            else{
                tvEmail.text = email
            }
            tvTelefono.text = telefono

            // Redirigir a la página de contacto
            tvUrl.setOnClickListener{
                 val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                (contexto as MainActivity?)!!.startActivity(i)
            }
        }

    }

    /**
     * Crea las nuevas vistas con los elementos seleccionados en forma de grid
     * @return los elementos con los datos
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        Log.i("oncreateviewholder", "a punto de hacer return")
        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, viewGroup, false))
    }

    /**
     * Despliega la información de la base de datos en la interfaz
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.i(
            "onbindviewholder",
            "a punto de obtener dataset y wardarlo en viewholder.textview.text"
        )

        // Deplegar información en la interfaz
        viewHolder.bind(
            dataSet[position].municipio.toString(),
            dataSet[position].url.toString(),
            dataSet[position].email.toString(),
            dataSet[position].telefono.toString(),
            contexto
        )
    }

    override fun getItemCount() = dataSet.size
}