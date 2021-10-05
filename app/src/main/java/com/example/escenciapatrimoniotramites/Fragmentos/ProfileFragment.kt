package com.example.escenciapatrimoniotramites.Fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.escenciapatrimoniotramites.R
import com.parse.ParseUser


class ProfileFragment : Fragment() {

    private val TAG : String = "ProfileActivity"
    private val LLAVE_PROFILE_PICTURE = "profilePicture"
    protected val currentUser : ParseUser = ParseUser.getCurrentUser()

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
            Log.i(TAG, "Entered btnCambiarPass")
        }
    }
}