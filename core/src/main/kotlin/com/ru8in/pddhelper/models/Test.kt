package com.ru8in.pddhelper.models

import com.ru8in.pddhelper.db.DatabaseManger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


@ExperimentalSerializationApi
fun main() {
    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

//    val ticket: Ticket
//    BufferedReader(FileReader(File("local/tickets/questions/A_B/tickets/Билет 1.json"))).use { ticket = json.decodeFromString<Ticket>(it.readText()) }
//    println(ticket)

//    val signs: Signs
//    BufferedReader(FileReader(File("local/tickets/signs/signs.json"))).use { signs = json.decodeFromString<Signs>(it.readText()) }
//    println(signs)

    val signs: Signs
    BufferedReader(FileReader(File("local/tickets/signs/signs.json"))).use { signs = json.decodeFromString<Signs>(it.readText()) }
    println(signs)
    println(json.encodeToString(signs))

    println()
    println(DatabaseManger.execute("SELECT * FROM UserAnswers"))
    println(DatabaseManger.execute("INSERT INTO UserAnswers (ticket, question, chosen_answer, is_right) VALUES (1, 1, 1, 1)"))
    println()
    println(DatabaseManger.execute("SELECT * FROM UserAnswers"))
}

