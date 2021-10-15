package com.example.escenciapatrimoniotramites.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.escenciapatrimoniotramites.R

/**
 * Gestiona la vista web que lleva a la donación
 */
class DonationsFragment : Fragment() {

    // Permite el uso y la lectura de JavaScript
    @SuppressLint("SetJavaScriptEnabled")

    /**
     * Se ejecuta al crear la vista
     * @return La vista de [DonationsFragment]
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donations, container, false)
    }

    /**
     * Una vez que la vista está creada, se genera una vista web
     * La vista web nos lleva al link de donación
     * @param myWebView Elemento de la interfaz donde se muestra el link
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        val myWebView: WebView = view.findViewById(R.id.wvPaypal)
        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String
            ): Boolean {
                view.loadUrl(url)
                Log.i("DonationFragment","entre")
                return true
            }
        }

        myWebView.loadUrl("https://www.paypal.com/donate?hosted_button_id=T6FJA2T992HPS")
        myWebView.settings.allowContentAccess = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.useWideViewPort = true
    }
}