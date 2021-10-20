package com.esenciapatrimonio.kampatramites.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.esenciapatrimonio.kampatramites.R

/**
 * Despliega la interfaz en caso de que no se encuentre una conexión a internet
 */
class ConnectionErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection_error)
        val btnReintentar : Button = findViewById(R.id.btnNoInternet)

        btnReintentar.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }

    }
}