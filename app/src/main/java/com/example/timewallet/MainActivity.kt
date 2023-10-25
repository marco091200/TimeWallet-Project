package com.example.timewallet

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.timewallet.Fragments.CalenderFragment
import com.example.timewallet.Fragments.CaptureFragment
import com.example.timewallet.Fragments.HomeFragment
import com.example.timewallet.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding
    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_RUN = "firstRun"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean(FIRST_RUN, true)

        if (isFirstRun) {
            displayFirstRunDialog { accepted ->
                if (accepted) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean(FIRST_RUN, false)
                    editor.apply()
                }
            }
        }

        viewBinding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragmentsManager(HomeFragment())
                R.id.capture -> fragmentsManager(CaptureFragment())
                R.id.calender -> fragmentsManager(CalenderFragment())
                else -> false
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
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    private fun displayFirstRunDialog(callback: (accepted: Boolean) -> Unit) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Herzlich Willkommen")
        builder.setMessage("Tauchen Sie in die Zukunft\nder Zeiterfassung ein.\nIhre Zeit, Ihr Erfolg!")
        builder.setPositiveButton("OK") { dialog, which ->
            callback(true)
        }
        val dialog = builder.create()
        dialog.show()
    }
}