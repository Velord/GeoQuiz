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

    val userAnswerMap = mutableMapOf<Question, Byte>()

    fun setQuestionIndex(index: Int) {
        if ((index > -1) && (index != indexComputer.currentIndex))
            indexComputer.currentIndex = index
    }

    private fun setUserAnswer(value: Byte) {
        if (!userAnswerMap.containsKey(questionBank[indexComputer.currentIndex])) {
            userAnswerMap += (questionBank[indexComputer.currentIndex] to value)
        } else {
            userAnswerMap[questionBank[indexComputer.currentIndex]] = value
        }
    }

    fun computeUserScoreInPercent(): Int =
        userAnswerMap.values.sum() * (100 / questionBank.size)


    fun isCorrectAnswer(userAnswer: Boolean): Int {

        val correctAnswer = questionBank[indexComputer.currentIndex].answer

        return if (correctAnswer == userAnswer) {
            setUserAnswer(1)
            R.string.correct_toast
        }
        else {
            setUserAnswer(0)
            R.string.incorrect_toast
        }
    }
}