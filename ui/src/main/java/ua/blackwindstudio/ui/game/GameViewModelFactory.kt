package ua.blackwindstudio.ui.game

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.blackwindstudio.domain.models.Difficulty
import ua.blackwindstudio.domain.repository.GameRepository
import ua.blackwindstudio.domain.usecases.GenerateQuestionCase
import ua.blackwindstudio.domain.usecases.GetGameSettingsCase

class GameViewModelFactory(
    private val difficulty: Difficulty,
    private val getGameSettingsCase: GetGameSettingsCase,
    private val generateQuestionCase: GenerateQuestionCase,
    application: Application
):
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        Log.i("Viewmodel_test", "factory triggered")
        return GameViewModel(difficulty, getGameSettingsCase, generateQuestionCase) as T
    }
}