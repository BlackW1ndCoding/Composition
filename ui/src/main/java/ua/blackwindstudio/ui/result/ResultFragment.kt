package ua.blackwindstudio.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.blackwindstudio.domain.models.GameResult
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.databinding.FragmentResultBinding
import ua.blackwindstudio.ui.utils.autoCleared
import java.lang.IllegalArgumentException

class ResultFragment: Fragment(R.layout.fragment_result) {
    private var binding by autoCleared<FragmentResultBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultBinding.bind(view)

        renderViewValues(
            GameResultArg.mapToGameResult(
                arguments?.getParcelable(GAME_RESULT_ARG)
                    ?: throw IllegalArgumentException("GameResult argument is required")
            )
        )

        binding.buttonTryAgain.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_chooseDifficultyFragment)
        }
    }

    private fun renderViewValues(result: GameResult) {
        with(binding) {
            textRequiredAnswers.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.required_number_of_right_answers_s),
                    result.gameSettings.minRightAnswersNumber
                )

            textScore.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.your_score_s),
                    result.rightAnswersCount
                )
            textRequiredRatio.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.required_ratio_of_right_answers_s),
                    result.gameSettings.minRightAnswersPercent
                )

            textYourRatio.text = String.format(
                getString(ua.blackwindstudio.resources.R.string.your_right_answers_ratio_s),
                calculateRightAnswersRation(result)
            )

        }
    }

    private fun calculateRightAnswersRation(result: GameResult) =
        (result.rightAnswersCount.toDouble() / result.totalQuestionsCount * 100).toInt()

    companion object {
        const val GAME_RESULT_ARG = "GAME_RESULT_ARG"
    }
}