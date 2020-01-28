package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        val correct = intent.getIntExtra("correct", 0)
        val total = intent.getIntExtra("total", 0)

        val textView = findViewById<TextView>(R.id.textResultView)
        textView.text = "$correct of $total correct answers!"
    }

    fun newQuiz(view: View) {
        Intent(this, QuizActivity::class.java).also {
            startActivity(it)
        }
    }
}
