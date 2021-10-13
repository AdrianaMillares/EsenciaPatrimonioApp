package com.example.escenciapatrimoniotramites.Activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.escenciapatrimoniotramites.R

/**
 * Despliega la interfaz en caso de que no se encuentre una conexión a internet
 */
class ConnectionErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection_error)
    }
}