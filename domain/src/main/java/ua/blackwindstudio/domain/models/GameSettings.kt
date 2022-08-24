package ua.blackwindstudio.domain.models

data class GameSettings(
    val maxSumValue: Int,
    val minRightAnswersNumber: Int,
    val minRightAnswersPercent: Int,
    val gameTimeInSeconds: Int
)
