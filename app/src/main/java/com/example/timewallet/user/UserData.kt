package com.example.timewallet.user

/**
 * Datenklasse zur Verwaltung von Benutzerdaten.
 *
 * @property benutzerName Der Name des Benutzers.
 * @property monatlicheArbeitsstunden Die monatlich verfügbaren Arbeitsstunden des Benutzers.
 *
 * @author Marco Martins
 * @created 19.11.2023
 */
data class UserData(
    var benutzerName: String,
    var monatlicheArbeitsstunden: String
)
