package com.example.oblig1

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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

    private val isRunningTest : Boolean by lazy {
        try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        }
        catch (e: ClassNotFoundException) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)


        if (isRunningTest) {
            shuffledData = listOf(
                Person(name="cage", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.nic_cage).toString()),
                Person(name="dog", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.dog).toString())
            )
            updateButton()
            showImage()
        }
        else {
            val viewModel = ViewModelProvider(this).get(PersonViewModel::class.java)

            viewModel.allPersons.observe(this, Observer {persons ->
                shuffledData = persons.shuffled()

                if (shuffledData.isEmpty()) {
                    Toast.makeText(this, "There are no persons!! Add some before starting the quiz", Toast.LENGTH_LONG).also {
                        it.show()
                    }
                    finish()
                }
                else {
                    updateButton()
                    showImage()
                }
            })

        }
    }

    private fun updateScoreText() {
        val text = findViewById<TextView>(R.id.score)

        text.text = getString(R.string.score_text).format(correct, currentIndex + 1)
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

        if (guess.equals(answer, ignoreCase = true)) {
            correct++
            guessView.setTextColor(ContextCompat.getColor(this, R.color.correctColor))
        }
        else {
            guessView.setTextColor(ContextCompat.getColor(this, R.color.wrongColor))
            val answerView = findViewById<TextView>(R.id.answer)
            answerView.text = getString(R.string.correct_answer).format(answer)
        }

        updateButton()
        updateScoreText()
    }

    private fun nextQuestion() {
        val answerView = findViewById<TextView>(R.id.answer)
        answerView.text = ""

        val guessView = findViewById<TextView>(R.id.guessText)
        guessView.text = ""
        guessView.setTextColor(ContextCompat.getColor(this, R.color.browser_actions_text_color))

        state = STATE.GUESSING

        nextPerson()
        updateButton()
        showImage()
    }

    private fun nextPerson() {
        if (currentIndex + 1 < shuffledData.size) {
            currentIndex++
        }
    }

    private fun showImage() {
        val imageView = findViewById<ImageView>(R.id.guessImage)
        if (currentIndex < shuffledData.size) {
            Glide
                .with(this)
                .load(shuffledData[currentIndex]?.filepath)
                .into(imageView)
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
