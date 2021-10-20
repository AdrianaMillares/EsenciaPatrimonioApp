package com.esenciapatrimonio.kampatramites.Fragmentos

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.esenciapatrimonio.kampatramites.R
import com.parse.ParseUser
import java.io.File
import java.io.IOException
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.parse.ParseException
import com.parse.ParseFile
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import android.widget.*
import com.esenciapatrimonio.kampatramites.Activities.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream

/**
 * Muestra el perfil del usuario
 * Permite cambiar la contraseña y la imagen del usuario
 * @param LLAVE_PROFILE_PICTURE llave que permite cambiar la foto de perfil
 * @param currentUser Usuario actual
 * @param photoFileName Nombre del archivo de la foto del usuario
 * @param selectedImagePath imagen seleccionada del carrete del usuario
 * @param ivProfile Elemento de la interfaz donde se almacena la imagen de perfil
 * @param tvUserProfile Elemento de la interfaz que despliega el nombre de usuario
 * @param tvEmailProfile Elemento de la interfaz que despliega el correo electrónico del usuario
 * @param btnCambiarPass Botón que al presionarlo permite cambiar la contraseña
 */
open class ProfileFragment : Fragment() {
    private val TAG: String = "ProfileActivity"
    private val LLAVE_PROFILE_PICTURE = "profilePicture"
    private val currentUser: ParseUser = ParseUser.getCurrentUser()
    private var photoFileName = "photo.jpg"
    private lateinit var selectedImagePath: String
    private lateinit var ivProfile: ImageView
    private lateinit var tvUserProfile: TextView
    private lateinit var tvEmailProfile: TextView
    private lateinit var btnCambiarPass: Button
    private lateinit var btnLogOut: Button



    /**
     * Se ejecuta al crear la vista
     * @return la vista del profilefragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    /**
     * Redirige a la vista de [LogIn]
     */
    private fun goLoginActivity() {
        Log.i(TAG, "Entered goMainActivity")
        val i = Intent(context, LoginActivity::class.java)
        startActivity(i)

        // Cierra todas las ventanas anteriores
        activity?.finishAffinity()
    }

    /**
     * Se ejecuta una vez que la vista esté creada despliega la información en los elementos correspondientes
     * Permite cambiar la contraseña
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfile = view.findViewById(R.id.ivProfile)
        tvUserProfile = view.findViewById(R.id.tvUserProfile)
        tvEmailProfile = view.findViewById(R.id.tvEmailProfile)
        btnCambiarPass = view.findViewById(R.id.btnCambiarPass)
        btnLogOut = view.findViewById(R.id.btnLogOut)
        btnLogOut.setOnClickListener {
            ParseUser.logOut()
            goLoginActivity()
        }

        Log.d(TAG, "username: " + currentUser.username + "email: " + currentUser.email)
        tvUserProfile.text = currentUser.username
        tvEmailProfile.text = currentUser.email
        Log.i(TAG, "url: " + currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url)
        Glide.with(this).load(currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url).circleCrop()
            .into(ivProfile)

        // Cambiar contraseña
        btnCambiarPass.setOnClickListener {
            val viewNewPass = View.inflate(context, R.layout.change_password, null)

            // Los componentes dentro de la alerta
            val btnCancelar: Button = viewNewPass.findViewById(R.id.btnCancelar)
            val btnAceptar: Button = viewNewPass.findViewById(R.id.btnAceptar)
            val etNewPassword: TextView = viewNewPass.findViewById(R.id.etNewPassword)
            val etNewPasswordVerif: TextView = viewNewPass.findViewById(R.id.etNewPasswordVerif)
            val builder = MaterialAlertDialogBuilder(requireContext()).setView(viewNewPass)
            val dialog = builder.create()
            dialog.show()

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnAceptar.setOnClickListener {
                val newPass: String = etNewPassword.text.toString()
                val newPassVerif: String = etNewPasswordVerif.text.toString()
                dialog.dismiss()
                cambiarContrasena(newPass, newPassVerif)
            }
        }

        ivProfile.setOnClickListener {
            cameraCheckPermissions()
        }
    }

    /**
     * Permite cambiar la contraseña
     * Despliega mensajes de alerta
     * @param newPass String que contiene la nueva contraseña
     * @param newPassVerif String que comprueba que la nueva contraseña es correcta
     */
    private fun cambiarContrasena(newPass: String, newPassVerif: String) {
        if (ProfileUtils.verificarCoincidenciaPasswords(newPass,newPassVerif)) {
            currentUser.setPassword(newPass)
            currentUser.saveInBackground {
                Toast.makeText(
                    requireContext(),
                    "Éxito! Tu contraseña ha sido cambiada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Permite cambiar la foto de perfil
     * @return la nueva imagen
     */
    private fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // Comprueba la versión del sistema operativo
            image = if (Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireActivity().contentResolver, photoUri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }


    /**
     * Abre una nueva ventana con la galeria del usuario para
     * elegir una foto y al ser elegida la lleva a otra funcion
     * donde será procesada
     */
    fun onPickPhoto() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            resultLauncher.launch(intent)
        }
    }

    /**
     * Respuesta cuando un usuario elige una foto de la galeria
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data: Intent? = result.data
                val selectedImageUri: Uri? = data!!.data
                selectedImagePath = selectedImageUri!!.path.toString()

                val selectedImage: Bitmap = loadFromUri(selectedImageUri)!!
                val stream = ByteArrayOutputStream()
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()

                changeProfilePicture(byteArray)
            }
        }

    /**
     * Revisa si el usuario ya dio los permisos necesarios para la aplicacion
     * Si no ha dado o negado permisos, pregunta por ellos
     * Si se le da los permisos se abre la galeria para elegir una imagen
     * Si no se dieron permisos, muestra un dialogo de ello
     */
    private fun cameraCheckPermissions() {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        onPickPhoto()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showRotationalDialogForPermission()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    /**
     * Muestra un mensaje para activar permisos desde configuracion
     * en caso de que el usuario no los tenga activados
     * Si presiona 'Ir a configuracion' lo llevo a la configuracion de la aplicacion
     * Si presiona 'Cancelar' no hace nada y se queda en la pagina de Perfil
     */
    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(context)
            .setMessage(
                "Parece ser que tienes los permisos apagados para esta funcionalidad."
                        + "Puedes encenderlas en configuraciones de la aplicación"
            )
            .setPositiveButton("Ir a configuración") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    /**
     * Cambia la foto de perfil en la base de datos
     * @param newProfilePic - Archivo de la nueva foto de perfil
     */
    private fun changeProfilePicture(newProfilePic: ByteArray) {
        Toast.makeText(context, "Guardando tu nueva foto de perfil...", Toast.LENGTH_LONG).show()
        val parseFile: ParseFile =
            ParseFile("foto.png" + File.separator + photoFileName, newProfilePic)
        parseFile.saveInBackground { e: ParseException? ->
            if (e == null) {
                //Save successfull
                Log.i(TAG, "Saved file successful")
            } else {
                // Something went wrong while saving
                Log.e(TAG, e.message!!)
            }
        }

        currentUser.put(LLAVE_PROFILE_PICTURE, ParseFile(newProfilePic))
        currentUser.saveInBackground { e: ParseException? ->
            val messageToShow = ProfileUtils.validateImageError(e)
            if (e == null) {
                // Save successful
                refreshPicture()
            }
            Toast.makeText(context, messageToShow , Toast.LENGTH_SHORT).show()


        }
    }

    /**
     * Actualiza la foto de perfil del usuario
     * con la nueva que acaba de cambiar
     */
    private fun refreshPicture() {
        Glide.with(this).load(currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url).circleCrop()
            .into(ivProfile)
    }

}
object ProfileUtils{
    public fun verificarCoincidenciaPasswords(newPass: String, newPassVerif: String) : Boolean{
        return (newPass==newPassVerif)
    }


    public fun validateImageError(e: ParseException?):String{
        if (e == null) {
            return ( "¡Tu nueva foto se ha guardado!")
        } else {
            return ("Vuelve a intentar más tarde, por favor.")

        }

    }

}