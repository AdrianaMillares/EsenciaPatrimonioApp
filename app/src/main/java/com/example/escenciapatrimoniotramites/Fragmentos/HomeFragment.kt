package com.example.escenciapatrimoniotramites.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.escenciapatrimoniotramites.Activities.LoginActivity
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseUser

class HomeFragment : Fragment() {

    val TAG : String = "HomeFragment"
    lateinit var btnLogOut : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogOut = view.findViewById(R.id.btnLogOut)

        btnLogOut.setOnClickListener {
            ParseUser.logOut()
            goLoginActivity()
        }
    }

    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(context, LoginActivity::class.java)
        startActivity(i)
        activity?.finishAffinity() // Cierra todas las ventanas anteriores
    }
}