package com.example.escenciapatrimoniotramites.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.escenciapatrimoniotramites.R
import com.parse.Parse
import com.parse.ParseException
import com.parse.ParseUser


class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    lateinit var etUsername:EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var tvSignUp:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Si el usuario ya esta loggeado, muestra la pantalla principal
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity()
        }

        // Se encuentran los componentes del layout
        etUsername = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogIn)
        tvSignUp = findViewById(R.id.tvSignUp)

        // Cuando el usuario da click, se verifican las credenciales de inicio de sesion
        btnLogin.setOnClickListener{
            Log.i(TAG, "onClick login button")
            var username = etUsername.text.toString()
            var password = etPassword.text.toString()
            Log.i(TAG, "username: $username password: $password")
            loginUser(username, password)
        }

        tvSignUp.setOnClickListener{
            goRegisterActivity()
        }
    }

    // Verifica las credenciales dadas comparandolas a las guardadas en la base de datos con Parse
    private fun loginUser(username: String, password: String) {
        Log.i(TAG, "loginUser: entre a la funcion")
        ParseUser.logInInBackground(username, password,
            ({ user, e ->
                Log.i(TAG, "logInInBackground: entered function")
                if (user != null) {
                    // Login fue exitoso
                    var currentUser = username;
                    Log.i(TAG, "loginUser: Wuwuwuw estoy loggeado")

                    goMainActivity();
                   // Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    // El login falló, ver ParseException para ver qué pasó
                    e.message?.let { Log.e(TAG, it) }
                    if (e.message == "username/email is required."){
                        Toast.makeText(this, "Se deben ingresar nombre de usuario/email", Toast.LENGTH_SHORT).show()
                    }
                    else if (e.message == "password is required."){
                        Toast.makeText(this, "Se debe ingresar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                    else if (e.message == "Invalid username/password."){
                        Toast.makeText(this, "El nombre de usuario o la contraseña no son correctos", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        )

    }

    ///////////




    //////////

    // Lleva al usuario a la pantalla principal
    private fun goMainActivity() {
        Log.i(TAG, "goMainActivity: Entered")
//        val i = Intent(this, MainActivity::class.java)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finishAffinity() // Cierra todas las ventanas anteriores
    }

    // Lleva al usuario a la pantalla de registro
    private fun goRegisterActivity() {
        Log.i(TAG, "goRegisterActivity: Entered")
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}