package ua.blackwindstudio.ui.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.blackwindstudio.domain.models.GameSettings

@Parcelize
data class GameSettingsArg(
    val maxSumValue: Int,
    val minRightAnswersNumber: Int,
    val minRightAnswersPercent: Int,
    val gameTimeInSeconds: Int
): Parcelable {
    companion object {
        fun mapFromGameSettings(gameSettings: GameSettings): GameSettingsArg {
            return GameSettingsArg(
                gameSettings.maxSumValue,
                gameSettings.minRightAnswersNumber,
                gameSettings.minRightAnswersPercent,
                gameSettings.gameTimeInSeconds
            )
        }

        fun mapToGameSettings(gameSettingsArg: GameSettingsArg): GameSettings {
            return GameSettings(
                gameSettingsArg.maxSumValue,
                gameSettingsArg.minRightAnswersNumber,
                gameSettingsArg.minRightAnswersPercent,
                gameSettingsArg.gameTimeInSeconds
            )
        }
    }
}