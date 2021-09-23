package com.example.escenciapatrimoniotramites.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.escenciapatrimoniotramites.Fragmentos.HomeFragment
import com.example.escenciapatrimoniotramites.Fragmentos.ProfileFragment
import com.example.escenciapatrimoniotramites.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    val TAG : String = "MainActivity"

    lateinit var btnLogOut : Button
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define your fragments here
        //val home: Fragment = FirstFragment()
        //val profile: Fragment = ProfileFragment()
        //val fragment3: Fragment = ThirdFragment()

        // Se encuentran los componentes de la pantalla
        //btnLogOut = findViewById(R.id.btnLogOut)
        bottomNav = findViewById(R.id.bottom_navigation)

        /*btnLogOut.setOnClickListener {
            ParseUser.logOut()
            goLoginActivity()
        }*/

        bottomNav.setOnItemSelectedListener { item ->
            //var fragment : Fragment
            when (item.itemId) {
                R.id.action_home ->{
                    val fragment = HomeFragment.newInstance()
                    openFragment(fragment)
                    true
                }         // do something here
                R.id.action_search ->{
                    val fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }       // do something here
                R.id.action_contacts -> {
                    val fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }      // do something here
                R.id.action_profile ->{
                    val fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }    // do something here
                R.id.action_donation ->{
                    val fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }         // do something here
                else -> true
            }
        }
    }

    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finishAffinity() // Cierra todas las ventanas anteriores
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}