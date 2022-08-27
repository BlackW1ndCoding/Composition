package ua.blackwindstudio.ui.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.blackwindstudio.domain.models.Difficulty

@Parcelize
enum class DifficultyArg: Parcelable {
    TEST, EASY, MEDIUM, HARD;

    companion object {
        fun mapFromDifficulty(difficulty: Difficulty): DifficultyArg {
            return when (difficulty) {
                Difficulty.TEST -> TEST
                Difficulty.EASY -> EASY
                Difficulty.MEDIUM -> MEDIUM
                Difficulty.HARD -> HARD
            }
        }

        fun mapToDifficulty(difficultyArg: DifficultyArg): Difficulty {
            return when (difficultyArg) {
                TEST -> Difficulty.TEST
                EASY -> Difficulty.EASY
                MEDIUM -> Difficulty.MEDIUM
                HARD -> Difficulty.HARD
            }
        }
    }
}