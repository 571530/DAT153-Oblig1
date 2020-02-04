package com.example.oblig1

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_set_name.*

class SetNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_name)
    }


    fun handleSaveName(view: View) {
        val tv = findViewById<TextView>(R.id.set_name_text)

        if (tv.text.isEmpty()) {
            Toast.makeText(this, "Your name cant be empty", Toast.LENGTH_LONG).also {
                it.show()
            }
        }
        else {
            val PREF_NAME = "name"
            val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()

            editor.putString(PREF_NAME, tv.text.toString())
            editor.commit()
            finish()
        }
    }
}
