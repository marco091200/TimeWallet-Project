package com.example.timewallet

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.timewallet.Fragments.CalenderFragment
import com.example.timewallet.Fragments.CaptureFragment
import com.example.timewallet.Fragments.HomeFragment
import com.example.timewallet.databinding.ActivityMainBinding

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
            // Zeigen Sie den Dialog an
            displayFirstRunDialog()

            // Setzen Sie den Wert auf false, um anzuzeigen, dass die App bereits geöffnet wurde
            val editor = sharedPreferences.edit()
            editor.putBoolean(FIRST_RUN, false)
            editor.apply()
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

    private fun displayFirstRunDialog() {
        val builder = AlertDialog.Builder(this, androidx.constraintlayout.widget.R.style.AlertDialog_AppCompat)
        builder.setTitle("Willkommen")
        builder.setMessage("Dies ist die erste Verwendung der App. Bitte gewähren Sie die erforderlichen Berechtigungen.")
        builder.setPositiveButton("OK") { dialog, which ->
            // Aktion bei Auswahl von OK
        }
        val dialog = builder.create()
        dialog.show()
    }
}