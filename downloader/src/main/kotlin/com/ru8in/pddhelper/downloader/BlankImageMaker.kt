package com.ru8in.pddhelper.downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

suspend fun createBlankQuestionImage(pathToPicture: String) {
    // Создаем пустое изображение с заданными размерами
    val width = 604
    val height = 225
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    // Задаем белый фон
    graphics.color = Color.WHITE
    graphics.fillRect(0, 0, width, height)

    // Настраиваем параметры текста
    graphics.color = Color.BLACK
    graphics.font = Font("Arial", Font.ITALIC, 27)
    val text = "Вопрос без рисунка"
    val fontMetrics = graphics.fontMetrics
    val x = (width - fontMetrics.stringWidth(text)) / 2
    val y = (height - fontMetrics.height) / 2 + fontMetrics.ascent

    // Рисуем текст в центре
    graphics.drawString(text, x, y)
    graphics.dispose()

    // Сохраняем изображение в файл
    val file = File("local/tickets/images/no_image.jpg")
    withContext(Dispatchers.IO) {
        ImageIO.write(image, "jpg", file)
    }
}