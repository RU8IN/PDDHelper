package com.ru8in.pddhelper.models

data class Statistic (
    val totalRightAnswersAmount: Int,
    val totalWrongAnswersAmount: Int,
    val particularQuestionsStats: Map<Question, Pair<Int, Int>>
)