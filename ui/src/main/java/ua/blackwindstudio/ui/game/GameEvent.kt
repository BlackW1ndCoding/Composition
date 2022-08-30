package ua.blackwindstudio.ui.game

import ua.blackwindstudio.domain.models.GameResult

sealed class GameEvent {
    data class GameOver(val gameResult: GameResult): GameEvent()
}
