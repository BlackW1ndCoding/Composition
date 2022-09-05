package ua.blackwindstudio.ui.choose_difficulty

import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.ui.databinding.FragmentChooseDifficultyBinding

fun FragmentChooseDifficultyBinding.setClickListeners(navigate: (Difficulty) -> Unit) {
    buttonDifficultyTest.setOnClickListener {
        navigate(Difficulty.TEST)
    }

    buttonDifficultyEasy.setOnClickListener {
        navigate(Difficulty.EASY)
    }

    buttonDifficultyMedium.setOnClickListener {
        navigate(Difficulty.MEDIUM)
    }

    buttonDifficultyHard.setOnClickListener {
        navigate(Difficulty.HARD)
    }
}
