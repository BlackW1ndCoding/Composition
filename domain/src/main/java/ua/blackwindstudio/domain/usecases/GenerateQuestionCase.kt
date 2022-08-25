package ua.blackwindstudio.domain.usecases

import ua.blackwindstudio.domain.repository.GameRepository
import ua.blackwindstudio.domain.models.Question

class GenerateQuestionCase(private val repository: GameRepository) {
    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}