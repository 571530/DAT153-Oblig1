package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
