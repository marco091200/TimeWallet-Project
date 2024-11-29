package com.example.timewallet.controls

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.example.timewallet.R
import com.example.timewallet.fragments.ProfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Die `ProfileFragementControl`-Klasse verwaltet das Öffnen und Schließen des Profil-Fragments sowie das Steuern der Sichtbarkeit der BottomNavigation.
 * Sie ermöglicht es, das Profil-Fragment anzuzeigen und zu verbergen, wenn das Profil-Icon oder der Schließ-Button betätigt wird.
 *
 * @author Marco Martins
 * @created 21.11.2023
 */
class ProfileFragementControl(
    private val fragmentManager: FragmentManager,
    private val bottomNavigationView: BottomNavigationView
) {

    /**
     * Öffnet das Profil-Fragment und versteckt die BottomNavigationView.
     */
    private fun openProfileFragment() {
        val profileFragment = ProfilFragment()
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout, profileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        bottomNavigationView.visibility = View.GONE
    }

    /**
     * Schließt das Profil-Fragment und zeigt die BottomNavigationView wieder an.
     */
    private fun closeProfileFragment() {
        fragmentManager.popBackStack()
    }

    /**
     * Setzt den Klick-Listener für das Profil-Icon, um das Profil-Fragment zu öffnen.
     */
    fun setupProfileIcon(profileIcon: ImageView) {
        profileIcon.setOnClickListener {
            openProfileFragment()
        }
    }

    /**
     * Setzt den Klick-Listener für den Schließ-Button, um das Profil-Fragment zu schließen.
     */
    fun setupCloseButton(closeButton: ImageView) {
        closeButton.setOnClickListener {
            closeProfileFragment()
        }
    }

}