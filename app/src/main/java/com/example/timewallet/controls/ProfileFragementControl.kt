package com.example.timewallet.controls

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.example.timewallet.R
import com.example.timewallet.fragments.ProfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragementControl (private val fragmentManager: FragmentManager, private val bottomNavigationView: BottomNavigationView){
    private fun openProfileFragment() {
        val profileFragment = ProfilFragment()
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout, profileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        bottomNavigationView.visibility = View.GONE
    }

    private fun closeProfileFragment() {
        fragmentManager.popBackStack()
    }

    fun setupProfileIcon(profileIcon: ImageView) {
        profileIcon.setOnClickListener {
            openProfileFragment()
        }
    }

    fun setupCloseButton(closeButton: ImageView) {
        closeButton.setOnClickListener {
            closeProfileFragment()
        }
    }
}