package ua.blackwindstudio.ui.game

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import ua.blackwindstudio.ui.model.GameStatus
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

    private val optionsList by lazy {
        val list = mutableListOf<TextView>()
        list.add(binding.textFirstAnswer)
        list.add(binding.textSecondAnswer)
        list.add(binding.textThirdAnswer)
        list.add(binding.textForthAnswer)
        list.add(binding.textFifthAnswer)
        list.add(binding.textSixthAnswer)
        list.toList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGameBinding.bind(view)

        setupStatusProgressBar()

        setEventListeners()
        setOnClickListeners()
    }

    private fun setupStatusProgressBar() {
        with(binding.statusProgress) {
            max = 100
            min = 0
            secondaryProgress = viewModel.gameSettings.minRightAnswersPercent
        }
    }

    private fun setEventListeners() {
        lifecycleScope.launch {
            viewModel.gameTimer.collectLatest { formattedTime ->
                updateGameTimer(formattedTime)
            }
        }

        lifecycleScope.launch {
            viewModel.gameStatus.collectLatest { status ->
                updateGameStatusViews(status)
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
    }

    private fun updateGameTimer(formattedTime: String) {
        binding.textTimer.text = formattedTime
    }

    private fun updateGameQuestionViews(question: Question?) {
        with(binding) {
            require(question != null)
            textSum.text = question.sum.toString()
            textVisibleNumber.text = question.visibleNumber.toString()

            val answers = question.options

            optionsList.forEachIndexed { index, view ->
                view.text = answers[index].toString()
            }
        }
    }

    private fun updateGameStatusViews(gameStatus: GameStatus) {
        with(binding) {
            textStatus.apply {
                text = String.format(
                    getString(ua.blackwindstudio.resources.R.string.text_status),
                    gameStatus.rightAnswersCount, viewModel.gameSettings.minRightAnswersNumber
                )
                setTextColor(
                    ColorStateList.valueOf(getColorByState(gameStatus.rightAnswersCountIsEnough))
                )
            }

            statusProgress.apply {
                progress = gameStatus.rightAnswersRatio
                progressTintList =
                    ColorStateList.valueOf(getColorByState(gameStatus.rightAnswersRatioIsEnough))
            }
        }
    }

    private fun setOnClickListeners() {
        optionsList.forEachIndexed { index, view ->
            view.setOnClickListener { viewModel.answerClicked(index) }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        val colorResId = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return requireContext().getColor(colorResId)
    }

    companion object {
        const val DIFFICULTY_LEVEL_ARG = "DIFFICULTY_LEVEL_ARG"
    }
}