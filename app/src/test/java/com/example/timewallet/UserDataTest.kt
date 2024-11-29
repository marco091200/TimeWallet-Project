package com.example.timewallet

import com.example.timewallet.user.UserData
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit-Tests für die Klasse [UserData].
 *
 * @author Marco Martins
 * @created 06.03.2024
 */

class UserDataTest {

    /**
     * Testfall zum Erstellen und Modifizieren einer [UserData]-Instanz.
     */
    @Test
    fun testUserData() {
        val userData = UserData("JohnDoe", "40")

        assertEquals("JohnDoe", userData.benutzerName)
        assertEquals("40", userData.monatlicheArbeitsstunden)

        userData.benutzerName = "JaneDoe"
        userData.monatlicheArbeitsstunden = "35"

        assertEquals("JaneDoe", userData.benutzerName)
        assertEquals("35", userData.monatlicheArbeitsstunden)
    }

    /**
     * Testfall zum Überprüfen der Gleichheit zwischen zwei [UserData]-Instanzen.
     */
    @Test
    fun testUserDataEquality() {
        val userData1 = UserData("JohnDoe", "40")
        val userData2 = UserData("JohnDoe", "40")

        assertEquals(userData1, userData2)
    }

    /**
     * Testfall zum Überprüfen der Ungleichheit zwischen zwei [UserData]-Instanzen.
     */

    @Test
    fun testUserDataInequality() {
        val userData1 = UserData("JohnDoe", "40")
        val userData2 = UserData("JaneDoe", "35")

        assertNotEquals(userData1, userData2)
    }
}