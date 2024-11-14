package com.ru8in.pddhelper.core

import com.ru8in.pddhelper.db.DatabaseManger
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class HelloApplication : Application() {
    private lateinit var stage: Stage

    override fun start(stage: Stage) {
        this.setStage(stage)
        this.setScene("primary-scene.fxml")
        this.loadImage("photo_2024-07-03_15-12-50.jpg")
    }

    private fun setStage(stage: Stage) {
        this.stage = stage
        stage.title = "Hello!"
        stage.show()
    }

    private fun setScene(path: String) {
        val fxmlLoader = FXMLLoader(this.javaClass.getResource(path))
        val scene = Scene(fxmlLoader.load())
        this.stage.scene = scene
    }

    private fun loadImage(path: String) {
        val icon = Image(this.javaClass.getResourceAsStream(path))
        this.stage.icons.add(icon)
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
//    DatabaseManger.execute("SELECT * FROM applications")
}