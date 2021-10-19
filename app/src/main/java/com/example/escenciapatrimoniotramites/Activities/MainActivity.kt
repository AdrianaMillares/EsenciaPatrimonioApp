package com.example.escenciapatrimoniotramites.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.escenciapatrimoniotramites.Fragmentos.*
import com.example.escenciapatrimoniotramites.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Maneja los distintos fragmentos de la interfaz y su navegación
 * @param btnLogOut Botón encargado de cerrar sesión
 * @param bottomNav Menú de navegación
 */
class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    lateinit var btnLogOut: Button
    private lateinit var bottomNav: BottomNavigationView

    /**
     * Se ejecuta al crear la vista, permite que se muestren las interfaces
     * @param homeFragment fragmento home
     * @param profileFragment fragmento profile
     * @param searchFragment fragmento search
     * @param contactsFragment fragmento contacts
     * @param donationsFragment fragmento donations
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //verificar conexion a internet
        if (InternetUtils.isNetworkAvailable(this) == false){
            val i = Intent(this, ConnectionErrorActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val searchFragment = SearchFragment()
        val contactsFragment = ContactsFragment()
        val donationsFragment = DonationsFragment()

        bottomNav = findViewById(R.id.bottomNavigation)
        openFragment(homeFragment)

        // Se muestran las interfaces correspondientes a la opción del menú
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    Log.i("openfragment", "cargando homefragment")
                    openFragment(homeFragment)
                    true
                }
                R.id.action_search -> {
                    openFragment(searchFragment)
                    true
                }
                R.id.action_contacts -> {
                    openFragment(contactsFragment)
                    true
                }
                R.id.action_profile -> {
                    openFragment(profileFragment)
                    true
                }
//                R.id.action_donation -> {
//                    openFragment(donationsFragment)
//                    true
//                }
                else -> {
                    openFragment(homeFragment)
                    true
                }
            }
        }
    }

    /**
     * Redirige al logIn
     */
    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        // Cierra todas las ventanas anteriores
        finishAffinity()
    }

    /**
     * Se encarga de abrir los fragmentos
     * @param fragmnt fragmento que se desea abrir
     */
    private fun openFragment(fragmnt: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.flContainer, fragmnt)
        frag.commit()
    }

    private fun onListItemClick(position: Int) {
        Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show()
    }
}

