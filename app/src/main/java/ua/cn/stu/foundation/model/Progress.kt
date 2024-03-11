package ua.cn.stu.foundation.model

sealed class Progress

object EmptyProgress : Progress()

data class PercentageProgress(
    val percentage: Int
) : Progress(){

    companion object {
        val START = PercentageProgress(percentage = 0)
    }

}


// Ниже будут реализованы доп extensions methods

fun Progress.isInProgress() = this !is EmptyProgress

fun Progress.getPercentage() = (this as? PercentageProgress)?.percentage ?: PercentageProgress.START.percentage