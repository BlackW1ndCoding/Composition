package ua.blackwindstudio.domain

import ua.blackwindstudio.domain.models.GameSettings
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.domain.models.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        optionsCount: Int
    ): Question

    fun getGameSettings(difficulty: Difficulty): GameSettings
}