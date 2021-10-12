package com.example.escenciapatrimoniotramites.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

//import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.escenciapatrimoniotramites.Activities.InformationActivity
import com.example.escenciapatrimoniotramites.Activities.LoginActivity
import com.example.escenciapatrimoniotramites.Activities.MainActivity
import com.example.escenciapatrimoniotramites.Modelos.Tramite
import android.widget.Button
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.escenciapatrimoniotramites.Activities.ViewMoreActivity
import com.example.escenciapatrimoniotramites.R
import com.google.android.material.internal.ContextUtils.getActivity
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseObject

import com.parse.ParseUser
import org.w3c.dom.Text


val listaTramites : ArrayList<Tramite> = ArrayList()
val listaLeyes : ArrayList<Tramite> = ArrayList()



class HomeFragment : Fragment()  {

    lateinit var recyclerView1: RecyclerView
    lateinit var recyclerView2: RecyclerView

    var list: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<*>
    lateinit var adapterLeyes: CustomAdapter
    lateinit var adapterTramites: CustomAdapter
    lateinit var tvVerTramites : TextView
    lateinit var tvVerLeyes : TextView

    val TAG : String = "HomeFragment"
    lateinit var btnLogOut : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        var vista = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView1=vista.findViewById(R.id.recyclerView1)
        recyclerView2=vista.findViewById(R.id.recyclerView2)
        tvVerLeyes=vista.findViewById(R.id.tvVerLeyes)
        tvVerTramites=vista.findViewById(R.id.tvVerTramites)


        var tempLayoutMan = LinearLayoutManager(activity)
        Log.i("rv","${recyclerView1.toString()}")
        Log.i("context","${activity.toString()}")
        Log.i("templayoutmanager","${tempLayoutMan.toString()}")
        //
//        recyclerView1.setLayoutManager(tempLayoutMan)





        return vista





    }
    //////////////
    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(context, LoginActivity::class.java)
        startActivity(i)
        activity?.finishAffinity() // Cierra todas las ventanas anteriores
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogOut = view.findViewById(R.id.btnLogOut)

        btnLogOut.setOnClickListener {
            ParseUser.logOut()
            goLoginActivity()
        }
        //listView = view.findViewById(R.id.listView)


        adapterTramites = CustomAdapter(listaTramites, requireContext(),
            { position -> onListItemClick(position) } )
        adapterLeyes = CustomAdapter(listaLeyes, requireContext(),
            { position -> onListItemClick(position) } )

        val query: ParseQuery<Tramite> = ParseQuery.getQuery(Tramite::class.java)

        if (listaTramites.size == 0 || listaLeyes.size == 0 ) {
            try {
                val itemList: List<Tramite> = query.find()
                for (tramite in itemList) {

                    // Revisa que el tramite no esté en la lista


                    if (tramite.nombre.toString() !in list) {
                        if (tramite.esTramite == true) {
                            //listaTramites.add(tramite.nombre.toString())
                            listaTramites.add(tramite)
                        } else {
                            //listaLeyes.add(tramite.nombre.toString())
                            listaLeyes.add(tramite)
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
        }



        recyclerView1 = view.findViewById(R.id.recyclerView1)
        recyclerView2 = view.findViewById(R.id.recyclerView2)


        val manager = LinearLayoutManager(requireContext())
        val manager2 = LinearLayoutManager(requireContext())

        manager.orientation = LinearLayoutManager.HORIZONTAL
        manager.scrollToPosition(0)

        manager2.orientation = LinearLayoutManager.HORIZONTAL
        manager2.scrollToPosition(0)

        recyclerView1.setLayoutManager(manager)
       // customAdapter = CustomAdapter(list, requireContext())
        recyclerView1.setHasFixedSize(true)

        recyclerView1.setAdapter(adapterTramites)
        recyclerView1.itemAnimator= DefaultItemAnimator()
        //recyclerView1.adapter= customAdapter


        recyclerView2.setLayoutManager(manager2)
        // customAdapter = CustomAdapter(list, requireContext())
        recyclerView2.setHasFixedSize(true)

        recyclerView2.setAdapter(adapterLeyes)
        recyclerView2.itemAnimator= DefaultItemAnimator()


        tvVerLeyes.setOnClickListener{
            val newActivity = Intent(context, ViewMoreActivity::class.java)
            newActivity.putExtra("categoria", "Leyes")
            newActivity.putExtra( "lista", listaLeyes  )
            //Log.i("listaLeyes", listaLeyes.size.toString())

            ( context as MainActivity?)!!.startActivity(newActivity)
        }

        tvVerTramites.setOnClickListener{
            val newActivity = Intent(context, ViewMoreActivity::class.java)
            newActivity.putExtra("categoria", "Trámites")
            newActivity.putExtra( "lista", listaTramites  )
          //  Log.i("listaLeyes", listaTramites.size.toString())

            ( context as MainActivity?)!!.startActivity(newActivity)
        }

    }

    private fun onListItemClick(strTramite: String) {
        //Toast.makeText(requireContext(), strTramite, Toast.LENGTH_SHORT).show()




    }


    //////////////
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

}

class CustomAdapter(private val dataSet: ArrayList<Tramite>, private val contexto: Context,private val onItemClicked: (strTramite: String) -> Unit) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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


