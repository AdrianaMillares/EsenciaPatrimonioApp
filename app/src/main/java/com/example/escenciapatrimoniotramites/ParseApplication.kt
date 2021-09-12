package com.example.escenciapatrimoniotramites

import android.app.Application
import com.parse.Parse

class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Register your parse models
        //ParseObject.registerSubclass(User.class);

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