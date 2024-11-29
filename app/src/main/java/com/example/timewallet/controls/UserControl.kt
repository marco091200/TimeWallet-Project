package com.example.timewallet.controls

import android.content.Context
import android.widget.Toast
import com.example.timewallet.user.UserData
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Diese Klasse verwaltet die Benutzerdaten und stellt Methoden zum Speichern und Laden der Benutzerdaten
 * aus einer lokalen Textdatei bereit.
 *
 * @author Marco Martins
 * @created 01.12.2023
 */
class UserControl(private val context: Context) {

    private val fileName = "userListData.txt"

    /**
     * Speichert die Benutzerdaten in einer Textdatei.
     *
     * @param user Das `UserData`-Objekt, das die Benutzerdaten enthält.
     */
    fun saveUserToTxt(user: UserData) {
        val fileContent = "${user.benutzerName},${user.monatlicheArbeitsstunden}\n"

        try {
            val file = File(context.filesDir, fileName)

            val fileOutputStream: FileOutputStream = if (file.exists()) {
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            } else {
                file.createNewFile()
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            }

            fileOutputStream.write(fileContent.toByteArray())
            fileOutputStream.close()
            Toast.makeText(context, "Erfolgreich gespeichert!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Fehler beim Speichern!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Liest die Benutzerdaten aus der Textdatei und gibt sie als `UserData`-Objekt zurück.
     * Wenn keine Daten gefunden werden, gibt es `null` zurück.
     *
     * @return Ein `UserData`-Objekt, das die Benutzerdaten enthält, oder `null` bei einem Fehler.
     */
    fun readUserFromTxt(): UserData? {
        var user: UserData? = null
        try {
            val fileInputStream = context.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(fileInputStream))
            val line: String? = reader.readLine()
            if (line != null) {
                val parts = line.split(",")
                if (parts.size == 2) {
                    user = UserData(parts[0], parts[1])
                }
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return user
    }

}