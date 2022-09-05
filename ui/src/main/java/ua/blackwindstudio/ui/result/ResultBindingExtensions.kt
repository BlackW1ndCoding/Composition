package ua.blackwindstudio.ui.result

import android.content.res.ColorStateList
import ua.blackwindstudio.domain.models.GameResult
import ua.blackwindstudio.ui.databinding.FragmentResultBinding

fun FragmentResultBinding.renderViewValues(result: GameResult) {
    val context = this.root.context
    imageFace.imageTintList =
        ColorStateList.valueOf(
            context.getColor(
                if (result.winner) android.R.color.holo_green_light else android.R.color.holo_red_light
            )
        )

    textRequiredAnswers.text =
        String.format(
            context.getString(ua.blackwindstudio.resources.R.string.required_number_of_right_answers_s),
            result.gameSettings.minRightAnswersNumber
        )

    textScore.text =
        String.format(
            context.getString(ua.blackwindstudio.resources.R.string.your_score_s),
            result.rightAnswersCount
        )
    textRequiredRatio.text =
        String.format(
            context.getString(ua.blackwindstudio.resources.R.string.required_ratio_of_right_answers_s),
            result.gameSettings.minRightAnswersPercent
        )

    textYourRatio.text = String.format(
        context.getString(ua.blackwindstudio.resources.R.string.your_right_answers_ratio_s),
        calculateRightAnswersRation(result)
    )
}

private fun calculateRightAnswersRation(result: GameResult) =
    (result.rightAnswersCount.toDouble() / result.totalQuestionsCount * 100).toInt()