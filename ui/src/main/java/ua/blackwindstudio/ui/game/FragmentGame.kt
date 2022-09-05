package ua.blackwindstudio.ui.game

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.blackwindstudio.data.GameRepositoryImpl
import ua.blackwindstudio.domain.usecases.GenerateQuestionCase
import ua.blackwindstudio.domain.usecases.GetGameSettingsCase
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.databinding.FragmentGameBinding
import ua.blackwindstudio.ui.utils.autoCleared

class FragmentGame: Fragment(R.layout.fragment_game) {
    private val args by navArgs<FragmentGameArgs>()
    private var binding by autoCleared<FragmentGameBinding>()
    private val viewModel by viewModels<GameViewModel> {
        GameViewModelFactory(
            args.difficultyLevelArg,
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

        binding.setupStatusProgressBar(viewModel)

        setEventListeners()
        setOnClickListeners()
    }

    private fun setEventListeners() {
        lifecycleScope.launch {
            viewModel.gameTimer.collectLatest { formattedTime ->
                binding.updateGameTimer(formattedTime)
            }
        }

        lifecycleScope.launch {
            viewModel.gameStatus.collectLatest { status ->
                binding.updateGameStatusViews(requireContext(), viewModel, status)
            }
        }

        lifecycleScope.launch {
            viewModel.question.collectLatest { question ->
                binding.updateGameQuestionViews(question, optionsList)
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

    private fun setOnClickListeners() {
        optionsList.forEachIndexed { index, view ->
            view.setOnClickListener { viewModel.answerClicked(index) }
        }
    }
}