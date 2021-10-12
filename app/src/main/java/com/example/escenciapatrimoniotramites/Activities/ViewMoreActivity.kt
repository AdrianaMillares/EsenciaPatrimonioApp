package com.example.escenciapatrimoniotramites.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.escenciapatrimoniotramites.Fragmentos.CustomAdapter
import com.example.escenciapatrimoniotramites.Fragmentos.listaLeyes
import com.example.escenciapatrimoniotramites.Fragmentos.listaTramites
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseException
import com.parse.ParseQuery

class ViewMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)
        val titulo : String
        var lista : ArrayList<Tramite>   = ArrayList()
        val list : ArrayList<String> = ArrayList()
        val intent = getIntent()
        val recyclerView : RecyclerView
        val tvTituloVerMas : TextView = findViewById(R.id.tvTituloVerMas)
         var adapterViewMore: CustomAdapter

        titulo = intent.extras?.getString("categoria").toString();
         val tempBool: Boolean
        if (titulo == "Trámites"){
             tempBool = true
        }
        else {
             tempBool = false
        }

        //lista = intent.extras!!.getParcelableArray("lista") as ArrayList<Tramite>
       // lista = intent.extras!!.getStringArrayList("lista") as ArrayList<Tramite>
        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)
        query.whereEqualTo("tramite", tempBool)

             try {
                val itemList: List<Tramite> = query.find()
                for (tramite in itemList) {

                    // Revisa que el tramite no esté en la lista


                    if (tramite.nombre.toString() !in list) {
                        if (tramite.esTramite == true) {
                            //listaTramites.add(tramite.nombre.toString())
                            lista.add(tramite)
                        } else {
                            //listaLeyes.add(tramite.nombre.toString())
                            lista.add(tramite)
                        }
                        list.add(tramite.nombre.toString())

                        Log.i("tramite", "$tramite.nombre.toString()")


                    } else {
                        continue
                    }
                }
                // customAdapter.updateList(list)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            //Thread.sleep(2_000)  // wait for 1 second


        tvTituloVerMas.text = titulo



         adapterViewMore = CustomAdapter(lista, this,
            { position -> onListItemClick(position) } )




        recyclerView = findViewById(R.id.rvViewMore)


        val manager = GridLayoutManager(this, 2)


        manager.scrollToPosition(0)



        recyclerView.setLayoutManager(manager)
        // customAdapter = CustomAdapter(list, requireContext())
        recyclerView.setHasFixedSize(true)

        recyclerView.setAdapter(adapterViewMore)
        recyclerView.itemAnimator= DefaultItemAnimator()
        //recyclerView1.adapter= customAdapter








    }

    private fun onListItemClick(strTramite: String) {
        //Toast.makeText(requireContext(), strTramite, Toast.LENGTH_SHORT).show()




    }

}

class ViewMoreAdapter(private val dataSet: ArrayList<Tramite>, private val contexto: Context, private val onItemClicked: (strTramite: String) -> Unit) :
    RecyclerView.Adapter<ViewMoreAdapter.ViewHolder>() {

    public fun updateList( items:ArrayList<Tramite> ){
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
    class ViewHolder(
        view: View,
        private val onItemClicked: (strTramite: String) -> Unit


    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView4)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(view: View) {

            val textView: TextView = view.findViewById(R.id.textView4)
            val strTramite = textView.text.toString()
            val i = Intent(view.context, LoginActivity::class.java)

            val intent = Intent(view.context , InformationActivity::class.java )
            intent.putExtra("nombreTramite", strTramite)

            (view.context as MainActivity?)!!.startActivity(intent)

            onItemClicked(strTramite)
        }
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

        return ViewHolder(layoutInflater.inflate( R.layout.text_row_item, viewGroup, false) , onItemClicked   )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.i("onbindviewholder","a punto de obtener dataset y wardarlo en viewholder.textview.text")

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position].nombre.toString(), contexto )
        viewHolder.textView.text = dataSet[position].nombre.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}
