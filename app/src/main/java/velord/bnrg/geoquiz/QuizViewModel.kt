package velord.bnrg.geoquiz

import androidx.lifecycle.ViewModel

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

    val userAnswerMap
            = mutableMapOf<Question, Pair<Byte, Boolean>>()

    var isCheater = false

    fun setQuestionIndex(index: Int) {
        if ((index > -1) && (index != indexComputer.currentIndex))
            indexComputer.currentIndex = index
    }

    private fun setUserAnswer(value: Byte, isCheat: Boolean) {
        if (!userAnswerMap.containsKey(questionBank[indexComputer.currentIndex])) {
            userAnswerMap += (questionBank[indexComputer.currentIndex]
                    to (value to isCheat ))
        } else {
            userAnswerMap[questionBank[indexComputer.currentIndex]] = (value to isCheat)
        }
    }

    fun computeUserScoreInPercent(): Int =
        userAnswerMap.values.fold(0) { f: Int , n: Pair<Byte, Boolean> ->
            if (n.second) f + 1 else f} *
                (100 / questionBank.size)


    fun isCorrectAnswer(userAnswer: Boolean): Int {

        val correctAnswer = questionBank[indexComputer.currentIndex].answer

        return when {
            isCheater -> {
                setUserAnswer(1, true)
                isCheater = false
                R.string.judgment_toast
            }
            userAnswerMap[questionBank[indexComputer.currentIndex]]?.second == true ->  {
                setUserAnswer(1, true)
                R.string.judgment_toast
            }
            correctAnswer == userAnswer ->  {
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