package ua.blackwindstudio.ui.game

import android.content.Context
import android.content.res.ColorStateList
import android.widget.TextView
import ua.blackwindstudio.domain.models.Question
import ua.blackwindstudio.ui.databinding.FragmentGameBinding
import ua.blackwindstudio.ui.model.GameStatus

fun FragmentGameBinding.updateGameTimer(formattedTime: String) {
    textTimer.text = formattedTime
}

fun FragmentGameBinding.setupStatusProgressBar(minRightAnswersRatio: Int) {
    with(statusProgress) {
        max = 100
        min = 0
        secondaryProgress = minRightAnswersRatio
    }
}

fun FragmentGameBinding.updateGameQuestionViews(question: Question?, optionsList: List<TextView>) {

    require(question != null)
    textSum.text = question.sum.toString()
    textVisibleNumber.text = question.visibleNumber.toString()

    val answers = question.options

    optionsList.forEachIndexed { index, view ->
        view.text = answers[index].toString()
    }
}

fun FragmentGameBinding.updateGameStatusViews(
    viewModel: GameViewModel,
    gameStatus: GameStatus
) {
    textStatus.apply {
        text = String.format(
            context.getString(ua.blackwindstudio.resources.R.string.text_status),
            gameStatus.rightAnswersCount, viewModel.gameSettings.minRightAnswersNumber
        )
        setTextColor(
            ColorStateList.valueOf(getColorByState(context, gameStatus.rightAnswersCountIsEnough))
        )
    }

    statusProgress.apply {
        progress = gameStatus.rightAnswersRatio
        progressTintList =
            ColorStateList.valueOf(getColorByState(this.context, gameStatus.rightAnswersRatioIsEnough))
    }
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return context.getColor(colorResId)
}