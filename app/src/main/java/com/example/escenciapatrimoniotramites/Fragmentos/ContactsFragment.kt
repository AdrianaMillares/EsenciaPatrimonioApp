package com.example.escenciapatrimoniotramites.Fragmentos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.escenciapatrimonioinstitutos.Modelos.Contactos
import com.example.escenciapatrimoniotramites.Activities.*
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseException
import com.parse.ParseQuery


class ContactsFragment : Fragment() {


    lateinit var recyclerView1: RecyclerView

    var list: ArrayList<String> = ArrayList()
    var listaContactos: ArrayList<Contactos> = ArrayList()
    lateinit var adapter: ContactsAdapter
    lateinit var tvMunicipio: TextView
    lateinit var tvUrl: TextView
    lateinit var tvEmail: TextView
    lateinit var tvTelefono: TextView

    val TAG: String = "HomeFragment"
    lateinit var btnLogOut: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_contacts, container, false)
/*
         tvMunicipio  = vista.findViewById(R.id.tvMunicipio)
         tvUrl  = vista.findViewById(R.id.tvUrl)
         tvEmail  = vista.findViewById(R.id.tvEmail)
         tvTelefono = vista.findViewById(R.id.tvTelefono)
*/
        var tempLayoutMan = LinearLayoutManager(activity)

        recyclerView1 = vista.findViewById(R.id.rvContactos)


        return vista
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }


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


                if (contacto.municipio.toString() !in list) {
                    listaContactos.add(contacto)

                    list.add(contacto.municipio.toString())
                    Log.i("query", contacto.municipio.toString())

                } else {
                    continue
                }
            }
            // customAdapter.updateList(list)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        recyclerView1 = view.findViewById(R.id.rvContactos)


        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.scrollToPosition(0)
        recyclerView1.setLayoutManager(manager)
        recyclerView1.setHasFixedSize(true)
        recyclerView1.setAdapter(adapter)
        recyclerView1.itemAnimator = DefaultItemAnimator()



    }
}


class ContactsAdapter(private val dataSet: ArrayList<Contactos>, private val contexto: Context) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    public fun updateList(items: ArrayList<Contactos>) {
        if (items != null && items.size > 0) {
            // dataSet.clear()
            //dataSet.addAll(items)

            for (tramite in items) {
                dataSet.add(tramite)
            }

            notifyDataSetChanged()
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(
        view: View

    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvMunicipio: TextView = view.findViewById(R.id.tvMunicipio)
        val tvUrl: TextView = view.findViewById(R.id.tvUrl)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)

        init {
            view.setOnClickListener(this)

        }

        override fun onClick(view: View) {

            //Todo: ver que hacer aqui jejejejejejeejejej
        }

        fun bind(
            municipio: String,
            url: String,
            email: String,
            telefono: String,
            contexto: Context
        ) {



            tvMunicipio.setText(municipio)
            tvUrl.setText(url)
            tvUrl.linksClickable = true

            if (email == "null"){
                tvEmail.setText(" ")}
            else{
                tvEmail.setText(email)
            }
            tvTelefono.setText(telefono)

            tvUrl.setOnClickListener{

                 val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)

                (contexto as MainActivity?)!!.startActivity(i)



            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var layoutInflater = LayoutInflater.from(viewGroup.context)
        // val view = LayoutInflater.from(viewGroup.context)
        //    .inflate(R.layout.text_row_item, viewGroup, false)
        Log.i("oncreateviewholder", "a punto de hacer return")

        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, viewGroup, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.i(
            "onbindviewholder",
            "a punto de obtener dataset y wardarlo en viewholder.textview.text"
        )

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(
            dataSet[position].municipio.toString(),
            dataSet[position].url.toString(),
            dataSet[position].email.toString(),
            dataSet[position].telefono.toString(),
            contexto
        )
        //viewHolder.textView.text = dataSet[position].nombre.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}

