package com.example.escenciapatrimoniotramites.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseUser
import android.content.Intent




class MainActivity : AppCompatActivity() {

    val TAG : String = "MainActivity"

    lateinit var btnLogOut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Se encuentran los componentes de la pantalla
        btnLogOut = findViewById(R.id.btnLogOut)

        btnLogOut.setOnClickListener {
            ParseUser.logOut()
            goLoginActivity()
        }
    }

    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finishAffinity() // Cierra todas las ventanas anteriores
    }
}