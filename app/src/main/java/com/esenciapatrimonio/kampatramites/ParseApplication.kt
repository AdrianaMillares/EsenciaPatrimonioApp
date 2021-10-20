package com.esenciapatrimonio.kampatramites

import android.app.Application
import com.esenciapatrimonio.escenciapatrimonioinstitutos.Modelos.Contactos
import com.esenciapatrimonio.kampatramites.Modelos.*
import com.parse.Parse
import com.parse.ParseObject

class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Register your parse models
        ParseObject.registerSubclass(Comentar::class.java)
        ParseObject.registerSubclass(Contactos::class.java)
        ParseObject.registerSubclass(Documentos::class.java)
        ParseObject.registerSubclass(Instituto::class.java)
        ParseObject.registerSubclass(Rol::class.java)
        ParseObject.registerSubclass(Seguir::class.java)
        ParseObject.registerSubclass(Tramite::class.java)

        // set applicationId, and server server based on the values in the back4app settings.
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_application_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }
}