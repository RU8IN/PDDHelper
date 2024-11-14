package com.ru8in.pddhelper.core

import com.ru8in.pddhelper.downloader.TicketsDownloader
import javafx.fxml.FXML
import javafx.scene.control.Button
import java.text.SimpleDateFormat
import java.util.*

class PrimaryScene {

    private var number = 0

    @FXML
    private lateinit var mainButton: Button

    @FXML
    private fun buttonClicked() {
        println("starting to download")
        var sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var currentDate = sdf.format(Date())
        println("starting to download: $currentDate")
        TicketsDownloader.downloadTicketsBlocking()
        sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        println("downloading ended: $currentDate")
    }
}