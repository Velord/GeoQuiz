package velord.bnrg.geoquiz.viewModel

import androidx.lifecycle.ViewModel
import velord.bnrg.geoquiz.R
import velord.bnrg.geoquiz.model.Cheat.isCheater
import velord.bnrg.geoquiz.model.IndexComputer
import velord.bnrg.geoquiz.model.Question

private const val TAG =  "QuizViewModel"

class QuizViewModel : ViewModel() {

    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val indexComputer = IndexComputer(0, questionBank.lastIndex, 0)

    val userAnswer
            = mutableMapOf<Question, Pair<Byte, Boolean>>()

    fun setQuestionIndex(index: Int) {
        if ((index > -1) && (index != indexComputer.currentIndex))
            indexComputer.currentIndex = index
    }

    private fun setUserAnswer(value: Byte, isCheat: Boolean) {
        if (!userAnswer.containsKey(questionBank[indexComputer.currentIndex])) {
            userAnswer += (questionBank[indexComputer.currentIndex]
                    to (value to isCheat ))
        } else {
            userAnswer[questionBank[indexComputer.currentIndex]] = (value to isCheat)
        }
    }

    fun computeUserScoreInPercent(): Int =
        userAnswer.values.fold(0) { f: Int , n: Pair<Byte, Boolean> ->
            if (n.second) f + 1 else f} *
                (100 / questionBank.size)


    fun isCorrectAnswer(answer: Boolean): Int {

        val correctAnswer = questionBank[indexComputer.currentIndex].answer

        return when {
            isCheater -> {
                setUserAnswer(1, true)
                isCheater = false
                R.string.judgment_toast
            }
            userAnswer[questionBank[indexComputer.currentIndex]]?.second == true ->  {
                setUserAnswer(1, true)
                R.string.judgment_toast
            }
            correctAnswer == answer ->  {
                setUserAnswer(1, false)
                R.string.correct_toast
            }
            else -> {
                setUserAnswer(0, false)
                R.string.incorrect_toast
            }
        }
    }
}