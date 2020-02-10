package com.example.oblig1


import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
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
import android.app.Activity
import android.app.Application
import android.app.Instrumentation.ActivityResult
import android.app.Instrumentation
import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.test.espresso.intent.Intents.intended
import androidx.test.platform.app.InstrumentationRegistry


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
                withId(R.id.button6),
                isDisplayed()
            )
        )

        val intent: Matcher<Intent> = allOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))

        val resources = ApplicationProvider.getApplicationContext<Application>().resources
        val imageUri = Uri.parse(
         ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
        resources.getResourcePackageName(R.drawable.nic_cage) + '/'.toString() +
        resources.getResourceTypeName(R.drawable.nic_cage) + '/'.toString() +
        resources.getResourceEntryName(R.drawable.nic_cage)
        )

        val resultData = Intent()
        resultData.data = imageUri
        val result = ActivityResult(
            Activity.RESULT_OK, resultData)

        Intents.init()
        intending(intent).respondWith(result)

        appCompatButton2.perform(click())

        intended(intent)
        Intents.release()

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
