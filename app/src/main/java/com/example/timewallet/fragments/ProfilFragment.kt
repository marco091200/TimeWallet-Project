package com.example.timewallet.fragments

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.ImagePickerControl
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.UserControl
import com.example.timewallet.dialogs.ImageDialog
import com.example.timewallet.user.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class ProfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val profileIcon = view.findViewById<ImageView>(R.id.profileOption)

        val closeButton = view.findViewById<ImageView>(R.id.closeButton)

        val userControl = UserControl(requireContext())

        profileControl = ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
        profileControl.setupCloseButton(closeButton)

        imagePicker = ImagePickerControl(this)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_profile)
            }
        }

        profileIcon.setOnClickListener {
            val imageDialog = ImageDialog()

            imageDialog.showImageDialog(requireContext()) { action ->
                // Hier wird die Logik je nach Aktion durchgeführt
                when (action) {
                    ImageDialog.ImageAction.ADD -> {
                        // Logik für das Hinzufügen eines Bildes
                        // Beispiel: Öffnen der Galerie oder Kamera
                        imagePicker.openGallery()
                    }
                    ImageDialog.ImageAction.DELETE -> {
                        // Logik für das Löschen des aktuellen Bildes
                        // Beispiel: Bild löschen
                        imagePicker.deleteImageFolder()
                        updateProfileIcon()
                    }
                    ImageDialog.ImageAction.OK -> {
                        // Logik für das Abbrechen oder Bestätigen
                        // Beispiel: Nichts tun oder Dialog schließen
                    }
                }
            }
        }


        val abspeicherButton = view.findViewById<MaterialButton>(R.id.datenSpeichern)
        val userName = view.findViewById<TextInputEditText>(R.id.benutzerName)
        val userHours = view.findViewById<TextInputEditText>(R.id.monatlicheArbeitsstunden)
        abspeicherButton.setOnClickListener {
            val newUser = UserData(userName.text.toString(), userHours.text.toString())
            userControl.saveUserToTxt(newUser)
        }
        val userList = userControl.readUserFromTxt()
        userName.setText(userList?.benutzerName)
        userHours.setText(userList?.monatlicheArbeitsstunden)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.handleActivityResult(
            requestCode,
            resultCode,
            data,
            view?.findViewById(R.id.profileOption) ?: return
        )
    }

    private fun updateProfileIcon() {
        val profileIcon = view?.findViewById<ImageView>(R.id.profileOption)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            if (savedImageFile != null) {
                profileIcon?.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon?.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            if (savedImageFile != null) {
                profileIcon?.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon?.setImageResource(R.drawable.img_profile)
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilFragment.--
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}