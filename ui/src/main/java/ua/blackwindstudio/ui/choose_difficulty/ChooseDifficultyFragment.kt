package ua.blackwindstudio.ui.choose_difficulty

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.DifficultyArg
import ua.blackwindstudio.ui.databinding.FragmentChooseDifficultyBinding
import ua.blackwindstudio.ui.utils.autoCleared

class ChooseDifficultyFragment: Fragment(R.layout.fragment_choose_difficulty) {
    private var binding by autoCleared<FragmentChooseDifficultyBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseDifficultyBinding.bind(view)

        addClickListeners()
    }

    private fun addClickListeners() {
        with(binding) {
            buttonDifficultyTest.setOnClickListener {
                navigateToGameFragment(Difficulty.TEST)
            }

            buttonDifficultyEasy.setOnClickListener {
                navigateToGameFragment(Difficulty.EASY)
            }

            buttonDifficultyMedium.setOnClickListener {
                navigateToGameFragment(Difficulty.MEDIUM)
            }

            buttonDifficultyHard.setOnClickListener {
                navigateToGameFragment(Difficulty.HARD)
            }
        }
    }

    private fun navigateToGameFragment(difficulty: Difficulty) {
        val action = ChooseDifficultyFragmentDirections
            .actionChooseDifficultyFragmentToFragmentGame(
                DifficultyArg.mapFromDifficulty(difficulty)
            )
        findNavController().navigate(action)
    }
}