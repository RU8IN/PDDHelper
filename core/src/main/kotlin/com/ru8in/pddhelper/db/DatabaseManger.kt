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

    init {
        this.getConnection()
        this.initiateTables()
    }

    private fun getConnection() {
        var count = 0
        val maxRetries = 2
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC")
                conn = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
                return
            } catch (ex: SQLException) {
                if (!File(DB_PATH).exists()) {
                    File(DB_PATH).parentFile.mkdirs()
                }
                if (++count == maxRetries) throw ex
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun initiateTables() {
        try {
            val scriptContent = this.javaClass.classLoader.getResource("initiate_db.sql")?.readText()
            if (scriptContent != null) {
                val queries = scriptContent.split(";")
                for (query in queries) {
                    this.execute(query.trim())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun execute(sql: String, parameters: Iterable<String> = emptyList()): ResultSet? {
        try {
            val statement = conn?.prepareStatement(sql)

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

