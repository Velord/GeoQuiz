package velord.bnrg.geoquiz

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import velord.bnrg.geoquiz.viewModel.QuizViewModel

class QuizViewModelTests: StringSpec() {

    private val viewModel = QuizViewModel()

    init {
        "setQuestionIndex" {
            forAll(Gen.Companion.choose(0, 100)) { n: Int ->
                val viewModel = QuizViewModel()
                viewModel.setQuestionIndex(n)
                viewModel.indexComputer.currentIndex == n
            }
        }

        "isCorrectAnswer" {
            forAll(Gen.choose(0, viewModel.questionBank.size)) { n ->
                val isCorrect = viewModel.questionBank[n].answer
                val isCorrectToast = viewModel.isCorrectAnswer(isCorrect)
                val toast = if(isCorrect)
                    isCorrectToast == R.string.correct_toast
                else
                    isCorrectToast == R.string.incorrect_toast

                toast
            }
        }

        "computeUserScoreInPercent" {
            viewModel.computeUserScoreInPercent() ==
                    viewModel.userAnswer.values.fold(0) { f: Int , n: Pair<Byte, Boolean> ->
                        if (n.second) f + 1 else f
                    } * (100 / viewModel.questionBank.size)
        }
    }
}