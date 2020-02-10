package com.example.oblig1


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import android.text.method.TextKeyListener.clear
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.preference.PreferenceManager


@LargeTest
@RunWith(AndroidJUnit4::class)
class QuizResultTest {

    private fun setSharedPrefs(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString("name", "nic cage")
        editor.commit()
    }

    @Rule
    @JvmField
    var mActivityTestRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            setSharedPrefs(ApplicationProvider.getApplicationContext())
            super.beforeActivityLaunched()
        }
    }

    @Test
    fun QuizResultTest() {
        val button = onView(
            allOf(
                withId(R.id.button2),
                isDisplayed()
            )
        )
        button.perform(click())

        val guessText = onView(
            allOf(
                withId(R.id.guessText),
                isDisplayed()
            )
        )

        guessText.perform(replaceText("cage"), closeSoftKeyboard())

        val nextButton = onView(
            allOf(
                withId(R.id.nextButton),
                isDisplayed()
            )
        )

        nextButton.perform(click())

        val score = onView(
            allOf(
                withId(R.id.score),
                isDisplayed()
            )
        )

        score.check(matches(withText("1 / 1 correct")))

        nextButton.perform(click())

        guessText.perform(replaceText("cage"), closeSoftKeyboard())

        nextButton.perform(click())

        score.check(matches(withText("1 / 2 correct")))
    }



}
