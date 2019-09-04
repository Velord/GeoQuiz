package velord.bnrg.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val EXTRA_ANSWER_IS_TRUE = "velord.bnrg.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "velord.bnrg.geoquiz.answer_is_shown"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView

    private val cheatViewModel by lazy {
        ViewModelProviders.of(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_textView)

        showApiLevel()

        showAnswerButton.setOnClickListener {
            answerTextViewSetText()
            cheatViewModel.isCheater = true
        }

        if (cheatViewModel.isCheater)
            answerTextViewSetText()
    }

    override fun onStart() {
        super.onStart()
        Log.d(velord.bnrg.geoquiz.TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(velord.bnrg.geoquiz.TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(velord.bnrg.geoquiz.TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(velord.bnrg.geoquiz.TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(velord.bnrg.geoquiz.TAG, "onDestroy() called")
    }

    private val answerTextViewSetText = {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    private val showApiLevel = {
        apiLevelTextView.setText("API Level ${Build.VERSION.SDK_INT}")
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
