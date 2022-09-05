package ua.blackwindstudio.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.domain.models.GameResult
import ua.blackwindstudio.domain.models.Question
import ua.blackwindstudio.domain.usecases.GenerateQuestionCase
import ua.blackwindstudio.domain.usecases.GetGameSettingsCase
import ua.blackwindstudio.ui.model.GameStatus
import ua.blackwindstudio.ui.utils.rightAnswer

class GameViewModel(
    difficulty: Difficulty,
    getGameSettingsCase: GetGameSettingsCase,
    private val generateQuestionCase: GenerateQuestionCase
): ViewModel() {
    private val _gameEvents = MutableSharedFlow<GameEvent>()
    val gameEvent: SharedFlow<GameEvent> = _gameEvents

    val gameSettings = getGameSettingsCase(difficulty)

    private val _gameTimer = MutableStateFlow("00:00")
    val gameTimer: StateFlow<String> = _gameTimer

    private val _question = MutableStateFlow(generateQuestionCase(gameSettings.maxSumValue))
    val question: StateFlow<Question> = _question

    private val _gameStatus = MutableStateFlow(
        GameStatus(
            0,
            0,
            false,
            0,
            false
        )
    )
    val gameStatus: StateFlow<GameStatus> = _gameStatus

    private val timer = createGameTimer()

    init {
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun answerClicked(position: Int) {
        val question = _question.value

        val answerIsRight =
            question.rightAnswer == question.options[position]

        updateGameStatus(answerIsRight)

        _question.value = generateQuestionCase(gameSettings.maxSumValue)
    }

    private fun updateGameStatus(answerIsRight: Boolean) {
        val oldStatus = _gameStatus.value
        val rightAnswersCount =
            if (answerIsRight) oldStatus.rightAnswersCount + 1 else oldStatus.rightAnswersCount
        val totalAnswersCount = oldStatus.totalAnswersCount + 1
        val rightAnswersIsEnough = rightAnswersIsEnough(rightAnswersCount)
        val rightAnswersRatio = calculateRightAnswersRatio(rightAnswersCount, totalAnswersCount)
        _gameStatus.update {
            GameStatus(
                rightAnswersCount,
                totalAnswersCount,
                rightAnswersIsEnough,
                rightAnswersRatio,
                rightAnswersRatioIsEnough(rightAnswersRatio)
            )
        }
    }

    private fun rightAnswersIsEnough(rightAnswers: Int): Boolean {
        return rightAnswers >= gameSettings.minRightAnswersNumber
    }

    private fun calculateRightAnswersRatio(rightAnswers: Int, totalAnswers: Int): Int {
        if (totalAnswers == 0) return 0
        return (rightAnswers.toDouble() / totalAnswers * 100).toInt()
    }

    private fun rightAnswersRatioIsEnough(rightAnswersRatio: Int): Boolean {
        return rightAnswersRatio >= gameSettings.minRightAnswersPercent
    }

    private fun isWinner(): Boolean {
        return try {
            val status = gameStatus.value
            (status.rightAnswersCount.toDouble() / status.totalAnswersCount * 100).toInt() >=
                    gameSettings.minRightAnswersPercent &&
                    status.rightAnswersCount >= gameSettings.minRightAnswersNumber
        } catch (e: ArithmeticException) {
            false
        }
    }

    private fun createGameTimer(): CountDownTimer {
        return object: CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _gameTimer.update { formatTime(millisUntilFinished) }
            }

            override fun onFinish() {
                gameOver()
            }
        }
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECOND
        val minutes = seconds / SECONDS_IN_MINUTE
        return String.format(
            "%02d:%02d", minutes, seconds % SECONDS_IN_MINUTE
        )
    }

    private fun gameOver() {
        timer.cancel()

        viewModelScope.launch {
            _gameEvents.emit(
                generateGameOverEvent()
            )
        }
    }

    private fun generateGameOverEvent(): GameEvent.GameOver {
        return GameEvent.GameOver(
            GameResult(
                winner = isWinner(),
                rightAnswersCount = gameStatus.value.rightAnswersCount,
                totalQuestionsCount = gameStatus.value.totalAnswersCount,
                gameSettings
            )
        )
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}