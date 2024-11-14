package com.ru8in.pddhelper.downloader

import kotlinx.coroutines.*

object TicketsDownloader {
    suspend fun downloadTickets(ticketsFolderPath: String = "local", keepZipArchive: Boolean = false) {
        ZipDownloader.downloadZipFile("$ticketsFolderPath/tickets.zip")
        FolderModifier.updateFolder(ticketsFolderPath, keepZipArchive)
        createBlankQuestionImage("$ticketsFolderPath/tickets/images/no_image.jpg")
    }

    fun downloadTicketsBlocking() = runBlocking {
        this@TicketsDownloader.downloadTickets()
    }
}

fun main(): Unit = runBlocking {
    TicketsDownloader.downloadTickets()
}