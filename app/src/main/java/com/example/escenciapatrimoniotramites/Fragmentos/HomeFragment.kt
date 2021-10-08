package com.example.escenciapatrimoniotramites.Fragmentos

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseObject




class HomeFragment : Fragment() {
    lateinit var recyclerView1: RecyclerView
    var list: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<*>
    lateinit var customAdapter: CustomAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        var vista = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView1=vista.findViewById(R.id.recyclerView1)
        var tempLayoutMan = LinearLayoutManager(activity)
        Log.i("rv","${recyclerView1.toString()}")
        Log.i("context","${activity.toString()}")
        Log.i("templayoutmanager","${tempLayoutMan.toString()}")
        //
//        recyclerView1.setLayoutManager(tempLayoutMan)
        return vista
    }
    //////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //listView = view.findViewById(R.id.listView)
        customAdapter = CustomAdapter(list, requireContext())

        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        // Execute the find asynchronously
/*        query.findInBackground { itemList, e ->
            if (e == null) {
                for (tramite in itemList) {

                    // Revisa que el tramite no esté en la lista
                    if(tramite.nombre.toString() !in list){
                        list.add(tramite.nombre.toString())

                        Log.i("tramite", "$tramite.nombre.toString()" )

                    }
                    else{ continue }
                }
            } else {
                Log.d("HomeFragment", "Error: " )
            }
            //customAdapter.updateList(list)
        }
        // Fetches data synchronously
        // Fetches data synchronously
*/
        try {
            val itemList: List<Tramite> = query.find()
            for (tramite in itemList) {

                // Revisa que el tramite no esté en la lista
                if(tramite.nombre.toString() !in list){
                    list.add(tramite.nombre.toString())

                    Log.i("tramite", "$tramite.nombre.toString()" )


                }
                else{ continue }
            }
           // customAdapter.updateList(list)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //Thread.sleep(2_000)  // wait for 1 second


        val appContext = requireContext().applicationContext
       // customAdapter = CustomAdapter(testArray)
        //adapter = ArrayAdapter<String>(appContext, android.R.layout.simple_list_item_1, list)
        //listView.adapter = adapter
        //val rview: RecyclerView = view.findViewById(R.id.recyclerView1)
        //rview.adapter = customAdapter
       // var testArray = Array(1){"hola"}

        recyclerView1 = view.findViewById(R.id.recyclerView1)
         val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.HORIZONTAL
        manager.scrollToPosition(0)
        recyclerView1.setLayoutManager(manager)
       // customAdapter = CustomAdapter(list, requireContext())
        recyclerView1.setHasFixedSize(true)

        recyclerView1.setAdapter(customAdapter)
        recyclerView1.itemAnimator= DefaultItemAnimator()
        //recyclerView1.adapter= customAdapter
    }
    //////////////
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}


class CustomAdapter(private val dataSet: ArrayList<String>, private val contexto: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    public fun updateList( items:ArrayList<String> ){
        if (items!= null && items.size > 0){
           // dataSet.clear()
            //dataSet.addAll(items)

            for (tramite in items ){
                dataSet.add(tramite)
            }

            notifyDataSetChanged()
        }
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView4)

         fun bind(str: String, contexto: Context) {
             textView.setText(str)
         }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var layoutInflater =LayoutInflater.from(viewGroup.context)
       // val view = LayoutInflater.from(viewGroup.context)
        //    .inflate(R.layout.text_row_item, viewGroup, false)
        Log.i("oncreateviewholder","a punto de hacer return")

        return ViewHolder(layoutInflater.inflate( R.layout.text_row_item, viewGroup, false)   )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.i("onbindviewholder","a punto de obtener dataset y wardarlo en viewholder.textview.text")

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position], contexto )
        viewHolder.textView.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)
var data =  listOf<String>()



/*
class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

}*/
