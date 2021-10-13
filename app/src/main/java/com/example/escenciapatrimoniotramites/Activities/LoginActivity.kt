package com.example.escenciapatrimoniotramites.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.escenciapatrimoniotramites.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parse.ParseException
import com.parse.ParseUser

/**
 * Gestiona el inicio de sesión en la aplicación, la verificación de usuarios y contraseña de los mismos.
 * @param etUsername Elemento de la interfaz que referencía al nombre del usuario ingresado
 * @param etPassword Elemento de la interfaz que referencía a la contraseña ingresada
 * @param btnLogin Elemento de la interfaz que referencía al botón que recupera al información al ser presionado
 * @param tvSingUp Elemento de la interfaz que referencía al botón que redirige al usuario al registro al ser presionado
 */
class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    lateinit var etUsername:EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var tvSignUp:TextView
    lateinit var tvForgotPassword:TextView

    /**
     * Se ejecuta al crear la vista, permite que se muestre la interfaz y recupera los datos
     * Si el usuario ya inicio sesión lo redirige a la pantalla de inicio de la aplicación
     * Verifica las credenciales de inicio de sesión
     * Redirige a la vista de registro
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

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

        tvForgotPassword.setOnClickListener {
            openModalResetPassword()
        }
    }

    /**
     * Abre un modal con un campo para introducir el correo del usuario
     * al que se le enviará un correo para realizar un reinicio de
     * contraseña. El correo contendrá instrucciones a seguir para hacer
     * este reinicio.
     */
    private fun openModalResetPassword() {
        // La vista que sera inflada dentro del alert dialog
        val viewReset = View.inflate(this, R.layout.reset_pass, null)

        // Los componentes dentro de la alerta
        val btnCancelarPassReset : Button = viewReset.findViewById(R.id.btnCancelarPassReset)
        val btnAceptarPassReset : Button = viewReset.findViewById(R.id.btnAceptarPassReset)
        val etMail : TextView = viewReset.findViewById(R.id.etMail)

        val builder = MaterialAlertDialogBuilder(this).setView(viewReset)
        val dialog = builder.create()
        dialog.show()

        btnCancelarPassReset.setOnClickListener {
            dialog.dismiss()
        }

        btnAceptarPassReset.setOnClickListener {
            var mail : String = etMail.text.toString()
            Log.i(TAG, "mail: $mail")
            dialog.dismiss()
            resetPassword(mail)
        }
    }

    /**
     * Realiza el envío de correo para resetear la contraseña del usuario
     * en caso de que la haya olvidado o por algún problema
     * @param mail correo del usuario
     */
    private fun resetPassword(mail: String) {
        ParseUser.requestPasswordResetInBackground(mail) { e: ParseException? ->
            if (e == null) {
                // An email was successfully sent with reset instructions.
                Log.i(TAG, "Correo enviado")
                Toast.makeText(this, "Se te envió un correo con las instrucciones", Toast.LENGTH_LONG).show()
            } else {
                // Something went wrong. Look at the ParseException to see what's up.
                Log.i(TAG, "Correo no enviado")
                Toast.makeText(this, "Hubo un error. Vuelve a intentarlo.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Verifica las credenciales dadas comparandolas a las guardadas en la base de datos con Parse
     * Una vez validados los datos se redirige a la pantalla de inicio de la aplicación
     * Si el usuario o la contraseña no coinciden se mostrará un mensaje de error
     * @param username referencía al nombre de usuario ingresado a la interfaz
     * @param password referencía a la contraseña ingresada a la interfaz
     */
    private fun loginUser(username: String, password: String) {
        Log.i(TAG, "loginUser: entre a la funcion")
        ParseUser.logInInBackground(username, password,
            ({ user, e ->
                Log.i(TAG, "logInInBackground: entered function")
                if (user != null) {
                    // Login fue exitoso
                    Log.i(TAG, "loginUser: Wuwuwuw estoy loggeado")
                    goMainActivity()
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

    /**
     * Redirige al usuario a la pantalla principal
     */
    private fun goMainActivity() {
        Log.i(TAG, "goMainActivity: Entered")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)

        // Cierra todas las ventanas anteriores
        finishAffinity()
    }

    /**
     * Redirige al usuario a la pantalla de registro
     */
    private fun goRegisterActivity() {
        Log.i(TAG, "goRegisterActivity: Entered")
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}