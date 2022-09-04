package ua.blackwindstudio.ui.model

data class GameStatus(
    val rightAnswersCount: Int,
    val totalAnswersCount: Int,
    val rightAnswersCountIsEnough: Boolean,
    val rightAnswersRatio: Int,
    val rightAnswersRatioIsEnough: Boolean
)
