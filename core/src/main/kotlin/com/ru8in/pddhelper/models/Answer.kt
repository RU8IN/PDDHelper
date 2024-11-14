package com.ru8in.pddhelper.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    @SerialName("answer_text")
    val answerText: String,
    @SerialName("is_correct")
    val isCorrect: Boolean
)