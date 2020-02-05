package com.example.oblig1


import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddAndRemoveTest {


    private fun setSharedPrefs(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString("name", "test mctest")
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

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.READ_EXTERNAL_STORAGE"
        )

    @Test
    fun addAndRemoveTest() {
        val appCompatButton = onView(
            allOf(
                withId(R.id.button3), withText("Add person"),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.button5), withText("Upload image"),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        // UPLOAD REQUIRED

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editText),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("test image"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.button4), withText("Add!!"),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Gå opp"),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.button), withText("Database"),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.remove_button), withText("Remove"),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.no_items), withText("Database is empty! Add some persons"),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Database is empty! Add some persons")))
    }
}
