package com.example.escenciapatrimoniotramites.Fragmentos

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
import com.example.escenciapatrimoniotramites.R
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private val TAG : String = "ProfileActivity"
    private val LLAVE_PROFILE_PICTURE = "profilePicture"
    protected val currentUser : ParseUser = ParseUser.getCurrentUser()

    var photoFileName = "photo.jpg"
    lateinit var selectedImagePath: String

    lateinit var ivProfile : ImageView
    lateinit var tvUserProfile : TextView
    lateinit var tvEmailProfile : TextView
    lateinit var btnCambiarPass : Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Encuentra los componentes de la vista
        ivProfile = view.findViewById(R.id.ivProfile)
        tvUserProfile = view.findViewById(R.id.tvUserProfile)
        tvEmailProfile = view.findViewById(R.id.tvEmailProfile)
        btnCambiarPass = view.findViewById(R.id.btnCambiarPass)

        // Establece el texto correspondiente al usuario loggeado
        Log.d(TAG, "username: " + currentUser.username + "email: " + currentUser.email)
        tvUserProfile.setText(currentUser.username)
        tvEmailProfile.setText(currentUser.email)
        Log.i(TAG, "url: " + currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url)
        Glide.with(this).load(currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url).circleCrop().into(ivProfile)

        // Funcionalidad de cambiar contrasena
        btnCambiarPass.setOnClickListener {
            // La vista que sera inflada dentro del alert dialog
            val viewNewPass = View.inflate(context, R.layout.change_password, null)

            // Los componentes dentro de la alerta
            val btnCancelar : Button = viewNewPass.findViewById(R.id.btnCancelar)
            val btnAceptar : Button = viewNewPass.findViewById(R.id.btnAceptar)
            val etNewPassword : TextView = viewNewPass.findViewById(R.id.etNewPassword)
            val etNewPasswordVerif : TextView = viewNewPass.findViewById(R.id.etNewPasswordVerif)


            val builder = MaterialAlertDialogBuilder(requireContext()).setView(viewNewPass)
            val dialog = builder.create()
            dialog.show()

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnAceptar.setOnClickListener {
                var newPass : String = etNewPassword.text.toString()
                var newPassVerif : String = etNewPasswordVerif.text.toString()
                dialog.dismiss()
                cambiarContrasena(newPass, newPassVerif)
            }
        }

        ivProfile.setOnClickListener {
            cameraCheckPermissions()
        }
    }


    private fun cambiarContrasena(newPass: String, newPassVerif: String) {
        if (newPass == newPassVerif) {
            currentUser.setPassword(newPass)
            currentUser.saveInBackground {
                Toast.makeText(requireContext(), "Éxito! Tu contraseña ha sido cambiada", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
    }


    fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            image = if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source = ImageDecoder.createSource(requireActivity().contentResolver, photoUri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
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
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data
            val selectedImageUri: Uri? = data!!.data

            // val selectedImageUri: Uri = attr.data
            selectedImagePath = selectedImageUri!!.path.toString()
            System.out.println("Image Path : $selectedImagePath")
            Log.d(TAG, "path $selectedImagePath")

            // Load the image located at photoUri into selectedImage
            val selectedImage: Bitmap = loadFromUri(selectedImageUri)!!
            val stream = ByteArrayOutputStream()
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            changeProfilePicture(byteArray)
        }
    }


    /**
     * Checa si el usuario ya dio los permisos necesarios para la aplicacion
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
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    /**
     * Muestra un mensaje para activar permisos desde configuracion
     * en caso de que el usuario no los tenga activados
     *
     * Si presiona 'Ir a configuracion' lo llevo a la configuracion de la aplicacion
     * Si presiona 'Cancelar' no hace nada y se queda en la pagina de Perfil
     */
    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(context)
            .setMessage("Parece ser que tienes los permisos apagados para esta funcionalidad."
                    + "Puedes encenderlas en configuraciones de la aplicación")
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
            .setNegativeButton("Cancelar") {dialog, _->
                dialog.dismiss()
            }.show()
    }


    /**
     * Cambia la foto de perfil en la base de datos
     * @param newProfilePic - Archivo de la nueva foto de perfil
     */
    private fun changeProfilePicture(newProfilePic: ByteArray) {

        var parseFile = ParseFile("foto.png"+ File.separator + photoFileName, newProfilePic)
        parseFile.saveInBackground { e: ParseException? ->
            if (e == null) {
                //Save successfull
                Log.i(TAG, "Saved file successful")
                refreshPicture()
            } else {
                // Something went wrong while saving
                Log.e(TAG, e.message!!)
            }
        }

        currentUser.put(LLAVE_PROFILE_PICTURE, ParseFile(newProfilePic))
        currentUser.saveInBackground { e: ParseException? ->
            if (e == null) {
                // Save successful
                Log.i(TAG, "Saved profile picture successful")
                Toast.makeText(context, "Save Successful", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, e.message!!)
                Log.i(TAG, "Save profile pic unsuccessful")
            }
        }
    }

    /**
     * Actualiza la foto de perfil del usuario
     * con la nueva que acaba de cambiar
     */
    private fun refreshPicture() {
        Glide.with(this).load(currentUser.getParseFile(LLAVE_PROFILE_PICTURE)?.url).circleCrop().into(ivProfile)
    }

}