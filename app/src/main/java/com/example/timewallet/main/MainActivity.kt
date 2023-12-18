package com.example.timewallet.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.databinding.ActivityMainBinding
import com.example.timewallet.dialogs.FirstRunDialog
import com.example.timewallet.fragments.CalenderFragment
import com.example.timewallet.fragments.CaptureFragment
import com.example.timewallet.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val prefsName = "MyPrefsFile"
    private val firstRun = "firstRun"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean(firstRun, true)

        if (isFirstRun) {
            FirstRunDialog.displayFirstRunDialog(this) { accepted ->
                if (accepted) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean(firstRun, false)
                    editor.apply()
                }
            }
        }

        viewBinding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragmentsManager(HomeFragment())
                R.id.capture -> fragmentsManager(CaptureFragment())
                R.id.calender -> fragmentsManager(CalenderFragment())
            }
            return@setOnItemSelectedListener true
        }
        if (savedInstanceState == null) {
            fragmentsManager(HomeFragment())
            viewBinding.bottomNavigation.selectedItemId = R.id.home
        }
    }

    private fun fragmentsManager(fragement:Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout,fragement)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}