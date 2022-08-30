package ua.blackwindstudio.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.domain.models.GameResult
import ua.blackwindstudio.domain.models.Question
import ua.blackwindstudio.domain.usecases.GenerateQuestionCase
import ua.blackwindstudio.domain.usecases.GetGameSettingsCase
import ua.blackwindstudio.ui.utils.rightAnswer
import java.lang.ArithmeticException

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

    private val _rightAnswersCount = MutableStateFlow(0)
    val rightAnswersCount: StateFlow<Int> = _rightAnswersCount

    private var totalQuestionsCount = 0

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

        if (question.rightAnswer == question.options[position]) _rightAnswersCount.value++
        totalQuestionsCount++

        if (checkGameOverCondition()) {
            gameOver()
        }
        _question.value = generateQuestionCase(gameSettings.maxSumValue)
    }

    private fun isWinner(): Boolean {
        return try {
            rightAnswersCount.value / totalQuestionsCount * 100 > gameSettings.minRightAnswersPercent
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
                _gameTimer.value = formatTime(millisUntilFinished)
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

    private fun checkGameOverCondition(): Boolean {
        return _rightAnswersCount.value == gameSettings.minRightAnswersNumber
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
                rightAnswersCount = _rightAnswersCount.value,
                totalQuestionsCount = totalQuestionsCount,
                gameSettings
            )
        )
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}