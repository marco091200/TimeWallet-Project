package com.example.timewallet.controls

import android.content.Context
import android.widget.Toast
import com.example.timewallet.user.UserData
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class UserControl(private val context: Context) {

    private val fileName = "userListData.txt"

    fun saveUserToTxt(user: UserData) {
        val fileContent = "${user.benutzerName},${user.monatlicheArbeitsstunden}\n"

        try {
            val file = File(context.filesDir, fileName)

            val fileOutputStream: FileOutputStream = if (file.exists()) {
                // Datei existiert bereits, öffne sie im Modus MODE_PRIVATE, um sie zu überschreiben
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            } else {
                // Datei existiert nicht, erstelle sie und füge den Eintrag hinzu
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


    fun readUserFromTxt(): UserData? {
        var user : UserData? = null
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