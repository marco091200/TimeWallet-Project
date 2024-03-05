package com.example.timewallet

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.timewallet.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * MainActivityInstrumentedTest.kt
 *
 * Dies ist eine Klasse für instrumentierte Tests der MainActivity.
 *
 * @author Marco Martins
 * @since 06-03-2024
 */


@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    // SharedPreferences-Namen und Schlüssel für den Erstdurchlauf
    private val prefsName = "FirstRunPreference"
    private val firstRun = "firstRun"

    /**
     * Vor jedem Test wird die MainActivity gestartet und der SharedPreferences-Wert für den Erstdurchlauf auf "false" gesetzt.
     */
    @Before
    fun launchActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(firstRun, false).apply()

        // Starte die MainActivity
        ActivityScenario.launch(MainActivity::class.java)
    }

    /**
     * Testet das Klicken auf die Bottom-Navigationselemente.
     */
    @Test
    fun testBottomNavigationClicks() {
        onView(withId(R.id.home)).perform(click())
        onView(withId(R.id.calender)).perform(click())
        onView(withId(R.id.capture)).perform(click())
    }

    /**
     * Testet das doppelte Klicken auf die Bottom-Navigationselemente.
     */
    @Test
    fun testDoubleNavigationClicks() {
        onView(withId(R.id.home))
            .perform(click())
            .perform(click())
        onView(withId(R.id.calender))
            .perform(click())
            .perform(click())
        onView(withId(R.id.capture))
            .perform(click())
            .perform(click())
    }

    /**
     * Testet, ob die richtigen Fragmente angezeigt werden, wenn auf die Bottom-Navigationselemente geklickt wird.
     */
    @Test
    fun testBottomNavigationShowRightFragment() {
        onView(withId(R.id.home)).perform(click())
        onView(withId(R.id.header_title_home)).check(matches(isDisplayed()))
        onView(withId(R.id.calender)).perform(click())
        onView(withId(R.id.header_title_calender)).check(matches(isDisplayed()))
        onView(withId(R.id.capture)).perform(click())
        onView(withId(R.id.header_title_capture)).check(matches(isDisplayed()))
    }
}
