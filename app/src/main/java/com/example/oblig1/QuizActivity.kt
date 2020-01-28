package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*


class QuizActivity : AppCompatActivity() {
    lateinit var shuffledData: List<Person>
    var currentIndex = 0
    var correct = 0

    enum class STATE {
        GUESSING,
        SHOW_ANSWER,
        FINISHED
    }

    var state = STATE.GUESSING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        shuffledData = PersonDatabase(application).all().shuffled()

        if (shuffledData.isEmpty()) {
            Toast.makeText(this, "There are no persons!! Add some before starting the quiz", Toast.LENGTH_LONG).also {
                it.show()
            }
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
        else {
            updateButton()
            showImage()
        }
    }


    private fun updateButton() {
        val nextButton = findViewById<Button>(R.id.nextButton)
        when(state) {
            STATE.GUESSING -> nextButton.text = getString(R.string.quiz_submit)
            STATE.SHOW_ANSWER -> nextButton.text = getString(R.string.quiz_next)
            STATE.FINISHED -> nextButton.text = getString(R.string.goto_result)
        }
    }

    private fun gotoResults(correct: Int, total: Int) {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("correct", correct)
        intent.putExtra("total", total)
        startActivity(intent)

    }

    fun handleButtonClick(view: View) {
        when(state) {
            STATE.GUESSING -> submitAnswer()
            STATE.SHOW_ANSWER -> nextQuestion()
            STATE.FINISHED -> gotoResults(correct, currentIndex + 1)
        }
    }

    private fun isFinished(): Boolean {
        return currentIndex == shuffledData.size - 1
    }

    private fun submitAnswer() {
        state = if (isFinished()) STATE.FINISHED else STATE.SHOW_ANSWER

        val guessView = findViewById<TextView>(R.id.guessText)
        val guess = guessView.text.toString()
        val answer = shuffledData[currentIndex].name

        if (guess == answer) {
            correct++
            guessView.setTextColor(resources.getColor(R.color.correctColor))
        }
        else {
            guessView.setTextColor(resources.getColor(R.color.wrongColor))
            val answerView = findViewById<TextView>(R.id.answer)
            answerView.text = getString(R.string.correct_answer).format(answer)
        }

        updateButton()
    }

    private fun nextQuestion() {
        val answerView = findViewById<TextView>(R.id.answer)
        answerView.text = ""

        val guessView = findViewById<TextView>(R.id.guessText)
        guessView.text = ""
        guessView.setTextColor(resources.getColor(R.color.browser_actions_text_color))

        nextPerson()
        updateButton()
        showImage()

        state = STATE.GUESSING
    }

    private fun nextPerson() {
        if (currentIndex + 1 < shuffledData.size) {
            currentIndex++
        }
    }

    private fun showImage() {
        val imageView = findViewById<ImageView>(R.id.guessImage)
        if (currentIndex < shuffledData.size) {
            imageView.setImageBitmap(shuffledData[currentIndex].image)
        }
    }

    fun stopQuizClick(view: View) {
        var total = currentIndex
        if (state == STATE.SHOW_ANSWER) {
            total++
        }
        gotoResults(correct, total)
    }
}
