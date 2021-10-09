package com.example.escenciapatrimoniotramites.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.escenciapatrimoniotramites.R
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebViewClient


class DonationsFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donations, container, false)

    }

    companion object {
        fun newInstance(): DonationsFragment = DonationsFragment()
    }

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
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.allowContentAccess = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.useWideViewPort = true
    }
}