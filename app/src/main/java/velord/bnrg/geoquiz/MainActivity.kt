package velord.bnrg.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import velord.bnrg.geoquiz.Cheat.canCheat
import velord.bnrg.geoquiz.Cheat.isCheater


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatTokenTextView: TextView

    private val quizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private val updateQuestionTextView: (Direction, Int) -> Unit = { direction, step ->
        //get index
        val newIndex = quizViewModel.indexComputer.compute(direction, step)
        //get resource id
        val questionTextResId = quizViewModel.questionBank[newIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")
        //init views
        findAllViewsById()
        //init views event
        initAllViewsEvent()
        //init ViewModel
        val quizViewModel = ViewModelProviders.of(this)
            .get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        //get index for question from saved instance state if exist and set it to ViewModel
        (savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0).apply {
            if (this != 0)
                quizViewModel.setQuestionIndex(this)
        }
        //set question to TextView
        updateQuestionTextView(Direction.NEXT, 0)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX,
            quizViewModel.indexComputer.currentIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_CODE_CHEAT) {
            isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false

            if (isCheater) {
                --Cheat.countCheatToken
                updateCheatTokenTextView()
            }

            if (!canCheat()) {
                cheatButton.isEnabled = false
            }
        }
    }

    private val initAllViewsEvent = {
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        cheatButton.setOnClickListener {view ->
            val answerIsTrue = quizViewModel.questionBank[
                    quizViewModel.indexComputer.currentIndex].answer
            val intent = CheatActivity.newIntent(this, answerIsTrue)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val option = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, option.toBundle())
            } else
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        nextButton.setOnClickListener {
            updateQuestionTextView(Direction.NEXT, 1)
        }

        prevButton.setOnClickListener {
            updateQuestionTextView(Direction.PREV, 1)
        }

        questionTextView.setOnClickListener {
            updateQuestionTextView(Direction.NEXT, 1)
        }

        updateCheatTokenTextView()
    }

    private val updateCheatTokenTextView = {
        cheatTokenTextView.setText("You have cheat: ${Cheat.countCheatToken}")
    }

    private val findAllViewsById = {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatTokenTextView = findViewById(R.id.cheat_token_textView)
    }

    private val informUserScore = {
        if (quizViewModel.userAnswerMap.size == quizViewModel.questionBank.size) {
            val msg = quizViewModel.computeUserScoreInPercent()
            Toast.makeText(this, "Your score is: $msg %", Toast.LENGTH_LONG).apply {
                setGravity(Gravity.TOP, 0, 150)
                show()
            }
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId = quizViewModel.isCorrectAnswer(userAnswer)
        informUserScore()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 120)
            show()
        }
    }
}