package com.ru8in.pddhelper.db

import com.ru8in.pddhelper.models.Question
import com.ru8in.pddhelper.models.Statistic
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.IllegalArgumentException

object PDDHelperAPI {
    private const val TICKETS_DIRECTORY = "local/tickets"
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    private inline fun <reified T> readModel(path: String): T {
        BufferedReader(FileReader(File(path))).use { return json.decodeFromString<T>(it.readText()) }
    }

    fun getQuestion(ticketNumber: Int = (1..40).random(), questionNumber: Int = (1..20).random()): Question {
        if (ticketNumber > 40 || ticketNumber < 1) {
            throw IllegalArgumentException("Ticket number must be between 1 and 40")
        }
        if (questionNumber > 20 || questionNumber < 1) {
            throw IllegalArgumentException("Question number must be between 1 and 20")
        }

        val questionsList =
            readModel<List<Question>>("$TICKETS_DIRECTORY/questions/A_B/tickets/Билет $ticketNumber.json")
        return questionsList.single { question: Question ->
            question.title == "Вопрос $questionNumber"
        }
    }

    fun saveAnswer(answer: Int, question: Question) {
        val ticketNumber = question.ticketNumber.split(" ").last()
        val questionNumber = question.title.split(" ").last()
        val isRight = answer == question.correctAnswer.split(" ").last().toInt()
        val sql = "INSERT INTO UserAnswers (ticket, question, chosen_answer, is_right) VALUES (?, ?, ?, ?)"
        DatabaseManger.execute(sql, listOf(ticketNumber, questionNumber, answer, isRight).map { it -> it.toString() })
    }

    fun getStatistics(): Statistic {
        val sql_total_right = "SELECT COUNT(id) FROM UserAnswers WHERE is_right = 'true'"
        val sql_total_wrong = "SELECT COUNT(id) FROM UserAnswers WHERE is_right = 'false'"
        val sql_particular =
            "SELECT\n" +
                    "    ticket as Ticket,\n" +
                    "    question AS Question,\n" +
                    "    SUM(CASE WHEN is_right = 'true' THEN 1 ELSE 0 END) AS RightAnswers,\n" +
                    "    SUM(CASE WHEN is_right = 'false' THEN 1 ELSE 0 END) AS WrongAnswers\n" +
                    "FROM UserAnswers\n" +
                    "GROUP BY ticket, question\n" +
                    "ORDER BY WrongAnswers desc\n" +
                    "LIMIT 10;\n"

        val totalRight = DatabaseManger.execute(sql_total_right)!!.getInt(1)
        val totalWrong = DatabaseManger.execute(sql_total_wrong)!!.getInt(1)
        val particularData = DatabaseManger.execute(sql_particular)
        val questionsStats = mutableMapOf<Question, Pair<Int, Int>>()

        if (particularData != null) {
            var ticket: Int
            var question: Int
            var rightAnswers: Int
            var wrongAnswers: Int
            while (particularData.next()) {
                ticket = particularData.getInt(1)
                question = particularData.getInt(2)
                rightAnswers = particularData.getInt(3)
                wrongAnswers = particularData.getInt(4)
                questionsStats[getQuestion(ticket, question)] = Pair(rightAnswers, wrongAnswers)
            }
        }
        return Statistic(totalRight, totalWrong, questionsStats)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        getStatistics().particularQuestionsStats.map {
            println("Question(${it.key.ticketNumber}-${it.key.title}: ${it.value.first} | ${it.value.second})")
        }
    }

}

