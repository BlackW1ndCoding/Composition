package ua.blackwindstudio.ui.game

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

class GameViewModel(
    difficulty: Difficulty,
    getGameSettingsCase: GetGameSettingsCase,
    private val generateQuestionCase: GenerateQuestionCase
): ViewModel() {
    private val _gameEvents = MutableSharedFlow<GameEvent>()
    val gameEvent: SharedFlow<GameEvent> = _gameEvents

    val gameSettings = getGameSettingsCase(difficulty)

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question

    private val _rightAnswersCount = MutableStateFlow(0)
    val rightAnswersCount: StateFlow<Int> = _rightAnswersCount

    private var totalAnswersCount = 0


    init {
        _question.value = generateQuestionCase(gameSettings.maxSumValue)
    }

    fun answerClicked(position: Int) {
        val value = _question.value!!
        if (value.sum ==
            value.visibleNumber + value.options[position - 1]
        ) _rightAnswersCount.value++
        totalAnswersCount++

        if (checkGameOverCondition()) {
            viewModelScope.launch {
                _gameEvents.emit(
                    generateGameOverEvent()
                )
            }
        }
        _question.value = generateQuestionCase(gameSettings.maxSumValue)
    }

    private fun generateGameOverEvent(): GameEvent.GameOver {
        return GameEvent.GameOver(
            GameResult(
                winner = rightAnswersCount.value / totalAnswersCount * 100 > gameSettings.minRightAnswersPercent,
                rightAnswersCount = _rightAnswersCount.value,
                totalQuestionsCount = totalAnswersCount,
                gameSettings
            )
        )
    }

    private fun checkGameOverCondition(): Boolean {
        return _rightAnswersCount.value == gameSettings.minRightAnswersNumber
    }
}