package velord.bnrg.geoquiz

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButoon: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )


    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButoon = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        falseButoon.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        nextButton.setOnClickListener { _ ->
            setNextQuestionTextView()
        }

        prevButton.setOnClickListener { _ ->
            setPreviousQuestionTextView()
        }

        questionTextView.setOnClickListener { _ ->
            setNextQuestionTextView()
        }

        setQuestionTextView()
    }

    private fun checkAnswer(userAnswer: Boolean) {

        val correctAnswer = questionBank[currentIndex].answer

        val messageResId =
            if (correctAnswer == userAnswer)
                R.string.correct_toast
            else
                R.string.incorrect_toast

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 120)
            show()
        }
    }

    private fun computeCurrentIndex(value: Int) {
        if (currentIndex == 0 && value < 0)
            currentIndex = questionBank.size - 1
        else
            currentIndex = (currentIndex + value) % questionBank.size
    }

    private fun setAnotherQuestionTextView(value: Int) {
        computeCurrentIndex(value)
        setQuestionTextView()
    }

    private val setPreviousQuestionTextView: () ->  Unit = {
        setAnotherQuestionTextView(-1)
    }


    private val setNextQuestionTextView: () ->  Unit = {
        setAnotherQuestionTextView(1)
    }



    private fun setQuestionTextView(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }
}