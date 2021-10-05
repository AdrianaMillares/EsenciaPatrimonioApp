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
import kotlinx.coroutines.CoroutineStart


class SearchFragment : Fragment() {

    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        listView = view.findViewById(R.id.listView)
        list = ArrayList()

        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        // Execute the find asynchronously
        query.findInBackground { itemList, e ->
            if (e == null) {
                for (tramite in itemList) {
                    list.add(tramite.nombre.toString())
                }
            } else {
                Log.d("SearchFragment", "Error: " )
            }

        }

        val appContext = requireContext().applicationContext
        adapter = ArrayAdapter<String>(appContext, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
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


}