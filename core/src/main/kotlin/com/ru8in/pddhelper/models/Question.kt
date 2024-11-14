package com.ru8in.pddhelper.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
        val title: String,
        @SerialName("ticket_number")
        val ticketNumber: String,
        @SerialName("ticket_category")
        val ticketCategory: String,
        @SerialName("image")
        val imagePath: String,
        val question: String,
        val answers: List<Answer>,
        @SerialName("correct_answer")
        val correctAnswer: String,
        @SerialName("answer_tip")
        val answerTip: String,
        val topic: List<String>,
        val id: String
)