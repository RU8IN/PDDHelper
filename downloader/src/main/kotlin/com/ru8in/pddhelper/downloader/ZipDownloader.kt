package com.ru8in.pddhelper.downloader

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import java.io.File
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.net.ConnectException
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

internal object ZipDownloader {

    private val REPO_URL = "https://api.github.com/repos/etspring/pdd_russia/releases/latest"
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
            expectSuccess = false
        }
    }

    private suspend fun getZipFileURL(): URL {
        @Serializable
        data class GitHubReleaseResponse(
            @SerialName("zipball_url") val downloadUrl: String
        )

        client.use {
            val response: HttpResponse = it.get(this.REPO_URL)
            val content: String = response.bodyAsText()
            try {
                val response = json.decodeFromString<GitHubReleaseResponse>(content)
                return URI.create(response.downloadUrl).toURL()
            } catch (ex: SerializationException) {
                throw IllegalArgumentException("Impossible to deserialize")
            }
        }
    }

    private suspend fun unzip(source: String, outputFolder: String) = coroutineScope {
        try {
            val zipFile = ZipFile(source)
            File(outputFolder).mkdirs()
            zipFile.extractAll(outputFolder)
        } catch (e: ZipException) {
            e.printStackTrace()
        }
    }

    suspend fun downloadZipFile(outputPath: String) {
        downloadFile(this.getZipFileURL(), outputPath)
        unzip(outputPath, File(outputPath).parent)
    }
}