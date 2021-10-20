package com.esenciapatrimonio.kampatramites.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.esenciapatrimonio.kampatramites.Activities.InformationActivity
import com.esenciapatrimonio.kampatramites.Activities.MainActivity
import com.esenciapatrimonio.kampatramites.Modelos.Tramite
import com.esenciapatrimonio.kampatramites.R
import com.parse.ParseQuery

/**
 * Obtiene la información de [Tramite]
 * Implementa la busqueda de [Tramite]
 * Despliega en pantalla los resultados en forma de lista
 * @param searchView Elemento de la interfaz que permite realizar la busqueda
 * @param listView Elemento de la interfaz donde se desplegara la información
 * @param list Lista de strings donde se insertan los nombres de [Tramite] para evitar duplicidad
 * @param adapter Permite enviar información a la interfaz
 */
class SearchFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    var list: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<*>

    /**
     * Se ejecuta al iniciar la vista
     * @param vista Contiene la interfaz de [SearchFragment]
     * @return la vista de la interfaz
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    /**
     * Se ejecuta al crear la vista
     * Obtiene y despliega el nombre de los tramites
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        listView = view.findViewById(R.id.listView)

        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        query.findInBackground { itemList, e ->
            if (e == null) {
                for (tramite in itemList) {

                    // Revisa que el tramite no esté en la lista
                    if(tramite.nombre.toString() !in list){
                        list.add(tramite.nombre.toString())
                    }
                    else{ continue }
                }
            } else {
                Log.d("SearchFragment", "Error: " )
            }
        }

        // Despliega los datos en la interfaz
        val appContext = requireContext().applicationContext
        adapter = ArrayAdapter<String>(appContext, R.layout.list_item_theme, list)
        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val newActivity = Intent(context, InformationActivity::class.java)
            newActivity.putExtra("nombreTramite", listView.getItemAtPosition(position).toString())
            newActivity.putExtra("imagenes",((1..7).random()).toString())

            ( context as MainActivity?)!!.startActivity(newActivity)
        }

        // Se filtran los elementos segun los caracteres que ingrese el usuario
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
}

