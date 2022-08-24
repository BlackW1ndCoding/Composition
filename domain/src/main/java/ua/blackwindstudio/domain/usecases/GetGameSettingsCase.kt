package ua.blackwindstudio.domain.usecases

import ua.blackwindstudio.domain.GameRepository
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.domain.models.GameSettings

class GetGameSettingsCase(private val repository: GameRepository) {
    operator fun invoke(difficulty: Difficulty): GameSettings {
        return repository.getGameSettings(difficulty)
    }
}