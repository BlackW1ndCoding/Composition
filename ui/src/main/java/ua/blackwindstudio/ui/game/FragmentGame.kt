package ua.blackwindstudio.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.blackwindstudio.data.GameRepositoryImpl
import ua.blackwindstudio.domain.models.Question
import ua.blackwindstudio.domain.usecases.GenerateQuestionCase
import ua.blackwindstudio.domain.usecases.GetGameSettingsCase
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.DifficultyArg
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.databinding.FragmentGameBinding
import ua.blackwindstudio.ui.utils.autoCleared

class FragmentGame(): Fragment(R.layout.fragment_game) {
    private var binding by autoCleared<FragmentGameBinding>()
    private val viewModel by viewModels<GameViewModel> {
        GameViewModelFactory(
            DifficultyArg.mapToDifficulty(
                arguments?.getParcelable(
                    DIFFICULTY_LEVEL_ARG
                ) ?: throw IllegalArgumentException("Must provide Difficulty argument")
            ),
            GetGameSettingsCase(GameRepositoryImpl),
            GenerateQuestionCase(GameRepositoryImpl),
            requireActivity().application,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGameBinding.bind(view)

        lifecycleScope.launch {
            viewModel.rightAnswersCount.collectLatest { rightAnswersCount ->
                updateGameStatusViews(rightAnswersCount)
            }
        }
        lifecycleScope.launch {
            viewModel.question.collectLatest { question ->
                updateGameQuestionViews(question)
            }
        }

        lifecycleScope.launch {
            viewModel.gameEvent.collectLatest { event ->
                when (event) {
                    is GameEvent.GameOver -> {
                        val action = FragmentGameDirections.actionFragmentGameToResultFragment(
                            GameResultArg.mapFromGameResult(
                                event.gameResult
                            )
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }
        setOnClickListeners()
    }

    private fun updateGameQuestionViews(question: Question?) {
        with(binding) {
            require(question != null)
            textSum.text = question.sum.toString()
            textVisibleNumber.text = question.visibleNumber.toString()

            val answers = question.options
            textFirstAnswer.text = answers[0].toString()
            textSecondAnswer.text = answers[1].toString()
            textThirdAnswer.text = answers[2].toString()
            textForthAnswer.text = answers[3].toString()
            textFifthAnswer.text = answers[4].toString()
            textSixthAnswer.text = answers[5].toString()
        }
    }

    private fun updateGameStatusViews(rightAnswersCount: Int) {
        with(binding) {
            textStatus.text = String.format(
                getString(ua.blackwindstudio.resources.R.string.text_status),
                rightAnswersCount, viewModel.gameSettings.minRightAnswersNumber
            )
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            textFirstAnswer.setOnClickListener { viewModel.answerClicked(1) }
            textSecondAnswer.setOnClickListener { viewModel.answerClicked(2) }
            textThirdAnswer.setOnClickListener { viewModel.answerClicked(3) }
            textForthAnswer.setOnClickListener { viewModel.answerClicked(4) }
            textFifthAnswer.setOnClickListener { viewModel.answerClicked(5) }
            textSixthAnswer.setOnClickListener { viewModel.answerClicked(6) }
        }
    }

    companion object {
        const val DIFFICULTY_LEVEL_ARG = "DIFFICULTY_LEVEL_ARG"
    }
}