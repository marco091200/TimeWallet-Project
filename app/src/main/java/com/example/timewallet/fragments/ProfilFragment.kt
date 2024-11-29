package com.example.timewallet.fragments

import com.example.timewallet.controls.ReminderControl
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.ImagePickerControl
import com.example.timewallet.controls.PermissionCheckerBattery
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.UserControl
import com.example.timewallet.dialogs.ImageDialog
import com.example.timewallet.dialogs.NoticeBeforeReminderDialog
import com.example.timewallet.dialogs.ReminderDialog
import com.example.timewallet.user.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

/**
 * ProfilFragment
 *
 * Ein Fragment, das das Benutzerprofil verwaltet. Es ermöglicht dem Benutzer, sein Profilbild zu ändern,
 * Benachrichtigungen zu verwalten und Benutzerdaten zu speichern. Es nutzt verschiedene Controls für
 * Bildauswahl, Erinnerungseinstellungen und Benutzerverwaltung.
 *
 * @author Marco Martins
 * @created 20.11.2023
 */
@Suppress("DEPRECATION")
class ProfilFragment : Fragment() {
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl


    /**
     * Lädt das Layout des Fragments und initialisiert alle UI-Elemente und Logik.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val profileIcon = view.findViewById<ImageView>(R.id.profileOption)
        val closeButton = view.findViewById<ImageView>(R.id.closeButton)


        val reminderDialog = ReminderDialog()
        val reminderButton = view.findViewById<Button>(R.id.benachrichtigungButton)

        val userControl = UserControl(requireContext())

        profileControl =
            ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
        profileControl.setupCloseButton(closeButton)

        imagePicker = ImagePickerControl(this)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            closeButton.setImageResource(R.drawable.img_white_cancel)
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            closeButton.setImageResource(R.drawable.img_cancel)
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_profile)
            }
        }

        profileIcon.setOnClickListener {
            val imageDialog = ImageDialog()

            imageDialog.showImageDialog(requireContext()) { action ->
                when (action) {
                    ImageDialog.ImageAction.ADD -> {
                        imagePicker.openGallery()
                    }

                    ImageDialog.ImageAction.DELETE -> {
                        imagePicker.deleteImageFolder()
                        updateProfileIcon()
                    }

                    ImageDialog.ImageAction.OK -> {
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

        val reminderControl = ReminderControl()
        reminderButton.setOnClickListener {
            if (PermissionCheckerBattery.isBackgroundActivityPermissionGranted(requireContext())) {
                reminderDialog.timeInputOpener(
                    parentFragmentManager, "Wann möchten Sie benachrichtigt werden?"
                ) { selectedTime ->
                    reminderControl.setDailyNotification(requireContext(), selectedTime)
                }
            } else {
                NoticeBeforeReminderDialog.displayBackgroundActivityDialog(requireContext())
            }
        }

        return view
    }

    /**
     * Wird aufgerufen, wenn die View des Fragments zerstört wird.
     * Hier wird die Sichtbarkeit der Bottom Navigation wiederhergestellt.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE
    }

    /**
     * Verarbeitet das Ergebnis einer Aktivität, die aus diesem Fragment gestartet wurde.
     * Diese Methode ist jedoch veraltet und kann durch die neue ActivityResultAPI ersetzt werden.
     */
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

    /**
     * Aktualisiert das Profilbild, wenn der Benutzer ein neues Bild hinzufügt oder löscht.
     */
    private fun updateProfileIcon() {
        val profileIcon = view?.findViewById<ImageView>(R.id.profileOption)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (savedImageFile != null) {
                profileIcon?.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon?.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            if (savedImageFile != null) {
                profileIcon?.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon?.setImageResource(R.drawable.img_profile)
            }
        }
    }
}