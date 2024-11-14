package com.ru8in.pddhelper.db

import com.ru8in.pddhelper.models.Answer
import com.ru8in.pddhelper.models.Question
import javafx.application.Application
import java.io.*
import java.sql.*
import kotlin.system.exitProcess


object DatabaseManger {
    private var conn: Connection? = null
    private const val DB_PATH = "local/db/database.sqlite"
    private const val MAX_RETRIES = 2
    private var retryCount = 0

    init {
        this.getConnection()
        this.initiateTables()
    }

    /**
     * Создает соединение с базой данных
     */
    private fun getConnection() {
        repeat(MAX_RETRIES) {
            try {
                // Создаем драйвер
                Class.forName("org.sqlite.JDBC")
                // Создаем соединение
                conn = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
                return
            } catch (ex: SQLException) {
                // Если файла не существует, создаем его
                if (!File(DB_PATH).exists()) {
                    File(DB_PATH).parentFile.mkdirs()
                }
                // Если количество попыток исчерпано, кидаем исключение
                if (++retryCount == MAX_RETRIES) throw ex
            } catch (ex: Exception) {
                // Если что-то пошло не так, выводим ошибку
                ex.printStackTrace()
            }
        }
    }

    /**
     * Инициализирует таблицы
     */
    private fun initiateTables() {
        try {
            // Читаем из ресурсов SQL-скрипт
            val scriptContent = this.javaClass.classLoader.getResource("initiate_db.sql")?.readText()
            if (scriptContent != null) {
                // Разделяем скрипт на отдельные запросы
                val queries = scriptContent.split(";")
                // Выполняем каждый из них
                for (query in queries) {
                    this.execute(query.trim())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Выполняет SQL-запрос
     * @param sql SQL-запрос
     * @param parameters параметры запроса
     * @return ResultSet, если запрос SELECT, null - если запрос UPDATE, INSERT, DELETE
     */
    fun execute(sql: String, parameters: Iterable<String> = emptyList()): ResultSet? {
        try {
            val statement = conn?.prepareStatement(sql)
            // Заполняем параметры
            for ((index, parameter) in parameters.withIndex()) {
                statement?.setString(index + 1, parameter)
            }

            if (sql.trim().startsWith("SELECT", ignoreCase = true)) {
                // Если запрос SELECT, возвращаем ResultSet
                return statement?.executeQuery()
            } else {
                // Иначе выполняем обновление (INSERT, UPDATE, DELETE)
                statement?.executeUpdate()
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(0)
        }
    }


}
