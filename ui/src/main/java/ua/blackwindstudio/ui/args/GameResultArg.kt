package ua.blackwindstudio.ui.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.blackwindstudio.domain.models.GameResult

@Parcelize
data class GameResultArg(
    val winner: Boolean,
    val rightAnswersCount: Int,
    val totalQuestionsCount: Int,
    val gameSettings: GameSettingsArg
): Parcelable {

    companion object {
        fun mapFromGameResult(gameResult: GameResult): GameResultArg {
            return GameResultArg(
                gameResult.winner,
                gameResult.rightAnswersCount,
                gameResult.totalQuestionsCount,
                GameSettingsArg.mapFromGameSettings(gameResult.gameSettings)
            )
        }

        fun mapToGameResult(gameResultArg: GameResultArg): GameResult {
            return GameResult(
                gameResultArg.winner,
                gameResultArg.rightAnswersCount,
                gameResultArg.totalQuestionsCount,
                GameSettingsArg.mapToGameSettings(gameResultArg.gameSettings)
            )
        }
    }
}
