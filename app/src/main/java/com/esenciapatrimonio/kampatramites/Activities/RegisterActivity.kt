package com.esenciapatrimonio.kampatramites.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.esenciapatrimonio.kampatramites.R
import com.parse.ParseUser

/**
 * Gestiona el registro de nuevos usuarios a la aplicación, agregandolos tambien a la base de datos
 * @param USERNAME_TAKEN Error que se muestra si el nombre de usuario ya está en uso
 * @param USER_EMAIL_TAKEN Error que se muestra si el correo electónico ya está en uso
 * @param etUsername Elemento de la interfaz que referencía al nombre del usuario ingresado
 * @param etMail Elemento de la interfaz que referencía al correo electrónico ingresado
 * @param etPassword Elemento de la interfaz que referencía a la contraseña ingresada
 * @param etVerifyPassword Elemento de la interfaz que referencía a la confirmación de la contraseña ingresada
 * @param btnRegister Elemento de la interfaz que referencía al botón que recupera al información al ser presionado
 * @param tvLogin Elemento de la interfaz que referencía al botón que redirige al usuario al LogIn al ser presionado
 */
class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"
    private val USERNAME_TAKEN: Int = 202
    private val USER_EMAIL_TAKEN: Int = 203

    private lateinit var etUsername: EditText
    private lateinit var etMail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etVerifyPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    /**
     * Se ejecuta al crear la vista, permite que se muestre la interfaz y recupera los datos
     * Corrobora que la contraseña cumpla las reglas de seguridad
     * Muestra mensajes de error
     * Inserta los datos en la base de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //Restringe la rotación automática
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //verificar conexion a internet
        if (InternetUtils.isNetworkAvailable(this) == false){
            val i = Intent(this, ConnectionErrorActivity::class.java)
            startActivity(i)

            // Cierra todas las ventanas anteriores
            finishAffinity()
        }
        etUsername = findViewById(R.id.etNombreReg)
        etMail = findViewById(R.id.etCorreoReg)
        etPassword = findViewById(R.id.etContraseñaReg)
        etVerifyPassword = findViewById(R.id.etConfContraseñaReg)
        btnRegister = findViewById(R.id.btnRegistro)
        tvLogin = findViewById(R.id.tvLogInReg)

        btnRegister.setOnClickListener {
            Log.i(TAG, "Presionado registro")
            val username = etUsername.text.toString()
            val mail = etMail.text.toString()
            val password = etPassword.text.toString()
            val passwordv = etVerifyPassword.text.toString()

            Log.i(TAG, "username: $username mail: $mail password: $password password v: $passwordv")

            // Mensajes de error
            when (RegisterUtils.validarInputs(username,password,passwordv,mail)) {
                "La contraseña debe de contener al menos 8 caracteres, y al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial" -> {
                    Toast.makeText(
                        this,
                        "La contraseña debe de contener al menos 8 caracteres, y al menos 1 mayúscula, 1 minúscula, 1 número y 1 caracter especial",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                "Debes de ingresar un correo valido" -> {
                    Toast.makeText(
                        this,
                        "Debes de ingresar un correo válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                "Las contraseñas deben de ser iguales" -> {
                    Toast.makeText(this, "Las contraseñas deben de ser iguales", Toast.LENGTH_SHORT)
                        .show()
                }
                "Debes de llenar todos los campos" -> {
                    Toast.makeText(this, "Debes de llenar todos los campos", Toast.LENGTH_SHORT).show()
                }
                "confirm" -> {
                    registerUser(username, mail, password)
                }
            }
        }
        tvLogin.setOnClickListener {
            goLoginActivity()
        }

    }

    /**
     * Inserta los datos ingresados por el usuario en la base de datos
     * Despliega mensajes de error si el usuario o el correo electónico ingresados está en uso
     * @param nombreusuario Nombre de usuario ingresado en la interfaz
     * @param mail Correo electrónico ingresado en la interfaz
     * @param password Contraseña ingresada en la interfaz
     */
    private fun registerUser(nombreusuario: String, mail: String, password: String) {
        Log.i(TAG, "registerUser: entre a la funcion")
        val user = ParseUser()

        with(user) {
            username = nombreusuario
            setPassword(password)
            email = mail

            signUpInBackground { e ->
                if (e == null) {
                    Log.i(TAG, "Al parecer todo salio bien, usuario: $username, correo: $email")
                    goLoginActivity()
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // El registro falló
                    when (e.code) {
                        USERNAME_TAKEN -> {
                            // El nombre de usuario está en uso
                            Toast.makeText(
                                applicationContext,
                                "El usuario ya está ocupado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        USER_EMAIL_TAKEN -> {
                            Toast.makeText(
                                applicationContext,
                                "El correo ya está ocupado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                applicationContext,
                                "Algo salió mal. Vuelve a intentarlo.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i(TAG, "No se que pudo salio mal :(")
                        }
                    }
                }
            }
        }
    }

    /**
     * Redirige al usuario a la pantalla principal
     */
    private fun goLoginActivity() {
        Log.i(TAG, "goLoginActivity: Entered")
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}

/**
 * Objeto para unit testing
 * Verifica los datos ingresados en la interfaz de [RegisterActivity]
 */
object RegisterUtils{
    fun validarInputs(username : String, password: String, passwordv: String, mail:String ) : String {
        val verifmail = Regex("""\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,6}""")

        // Verifica que la contraseña contenga al menos 8 caracteres, un número, una mayúscula, una minuscula y un caracter especial
        val verifpass = Regex("^(?=.*\\d)(?=.*[\\W])(?=.*[A-Z])(?=.*[a-z])\\S{8,99}\$")
        if (username != "" && mail != "" && password != "" && passwordv != "") {
            return if (password == passwordv) {
                if (verifmail.containsMatchIn(mail)) {
                    if (verifpass.containsMatchIn(password)) {
                        "confirm"
                        //registerUser(username, mail, password)
                    } else {
                        ("La contraseña debe de contener al menos 8 caracteres, y al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial")
                    }
                } else {
                    ("Debes de ingresar un correo valido")
                }
            } else {
                ("Las contraseñas deben de ser iguales")
            }
        } else {
            return ("Debes de llenar todos los campos")
        }
    }
}