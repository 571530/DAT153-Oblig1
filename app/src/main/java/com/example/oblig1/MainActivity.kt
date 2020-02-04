package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.let {

        }


        if (!isNameSet()) {
            Intent(this, SetNameActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.settings) {
            Intent(this, SettingsActivity::class.java).also {
                startActivity(it)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun isNameSet(): Boolean {
        val PREF_NAME = "name"

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val name = sharedPref.getString(PREF_NAME, "")
        return name.isNotEmpty()
    }

    fun startDatabase(view: View) {
        val intent = Intent(this, DatabaseActivity::class.java)
        startActivity(intent)
    }

    fun startAdd(view: View) {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    fun startQuiz(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
