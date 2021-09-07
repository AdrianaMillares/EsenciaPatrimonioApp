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
                .applicationId("VHDJU5sMranwvEskz5dBcEgygPkXtk3mnj7jSrb9")
                .clientKey("n3YqII94rqfjdvQHuEbl37HTQkHXwCBuAWrfM0RN")
                .server("https://parseapi.back4app.com")
                .build()
        )
    }
}