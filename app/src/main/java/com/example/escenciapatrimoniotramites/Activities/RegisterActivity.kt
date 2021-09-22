package com.example.escenciapatrimoniotramites.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseUser

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"
    lateinit var etUsername: EditText
    lateinit var etMail: EditText
    lateinit var etPassword: EditText
    lateinit var etVerifyPassword: EditText
    lateinit var btnRegister: Button
    lateinit var tvLogin:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etNombreReg)
        etMail = findViewById(R.id.etCorreoReg)
        etPassword = findViewById(R.id.etContraseñaReg)
        etVerifyPassword = findViewById(R.id.etConfContraseñaReg)
        btnRegister = findViewById(R.id.btnRegistro)
        tvLogin = findViewById(R.id.tvLogInReg)

        btnRegister.setOnClickListener{
            Log.i(TAG, "Presionado registro")
            var username = etUsername.text.toString()
            var mail = etMail.text.toString()
            var password = etPassword.text.toString()
            var passwordv = etVerifyPassword.text.toString()
            var verifmail = Regex("""\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,6}""")
//            var number = Regex("[0-9]")
//            var may = Regex("[A-Z]")
//            var min = Regex("[a-z]")
//            var esp = Regex("\\W")
            var verifpass = Regex("^(?=.*\\d)(?=.*[\\W])(?=.*[A-Z])(?=.*[a-z])\\S{8,99}\$")


            Log.i(TAG, "username: $username mail: $mail password: $password password v: $passwordv")

            if(username!="" && mail!="" && password!="" && passwordv!=""){
                if (password==passwordv){
                    if (verifmail.containsMatchIn(mail)){
                        if (verifpass.containsMatchIn(password)){
                            Log.i(TAG, "Ya jalo tu registro")
                            registerUser(username, mail, password)
                        } else {
                            Log.i(TAG, "Acaso quieres que te hacken?")
                            Toast.makeText(this, "La contraseña debe de contener al menos 8 caracteres, y al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.i(TAG, "Ingresa un correo valido plz")
                        Toast.makeText(this, "Debes de ingresar un correo valido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.i(TAG, "las contras deben de ser iguales")
                    Toast.makeText(this, "Las contraseñas deben de ser iguales", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.i(TAG, "Debes de llenar todo")
                Toast.makeText(this, "Debes de llenar todos los campos", Toast.LENGTH_SHORT).show()
            }




        }

        tvLogin.setOnClickListener{
            goLoginActivity()
        }

    }

    private fun registerUser(nombreusuario: String, mail:String, password: String) {
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
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()

                } else {
                    Log.i(TAG, "No se que pudo salio mal :(")
                    Toast.makeText(applicationContext, "Algo falló, vuelve a intentarlo", Toast.LENGTH_SHORT).show()

                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        }

    }


    private fun goLoginActivity() {
        Log.i(TAG, "goLoginActivity: Entered")
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}