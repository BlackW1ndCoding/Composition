package ua.blackwindstudio.domain.models

data class GameResult(
    val winner: Boolean,
    val rightAnswersCount: Int,
    val totalQuestionsCount: Int,
    val gameSettings: GameSettings
)
