package com.example.escenciapatrimoniotramites.Fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.example.escenciapatrimoniotramites.R
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.parse.ParseQuery
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent

import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.example.escenciapatrimoniotramites.Activities.InformationActivity
import com.example.escenciapatrimoniotramites.Activities.MainActivity
import android.widget.TextView

import android.widget.AdapterView.OnItemClickListener





class SearchFragment : Fragment() {

    lateinit var searchView: SearchView
    lateinit var listView: ListView
    var list: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        listView = view.findViewById(R.id.listView)


        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        // Execute the find asynchronously
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

        val appContext = requireContext().applicationContext
        adapter = ArrayAdapter<String>(appContext, R.layout.list_item_theme, list)
        listView.adapter = adapter
        //listView.onItemClickListener = onItemClick()

        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->


          /*  fun onItemClick(adapter : ArrayAdapter<*>, v: View, pos: Int) {
                val currentName= listView.selectedItem.toString()
            }*/

            val mTextView = view as TextView
            val newActivity = Intent(context, InformationActivity::class.java)
            newActivity.putExtra("nombreTramite", listView.getItemAtPosition(position).toString())

            // newActivity.putExtra("nombreTramite", list[position])
            ( context as MainActivity?)!!.startActivity(newActivity)


        })


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

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

/*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
 */
fun onItemClick(
    parent: AdapterView<*>?, view: View?,
    position: Int, id: Long
) {
    val newActivity = Intent(context, InformationActivity::class.java)
    newActivity.putExtra("nombreTramite", list[position])
    ( context as MainActivity?)!!.startActivity(newActivity)



}

}

