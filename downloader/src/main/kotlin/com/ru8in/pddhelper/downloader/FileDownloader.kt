package com.ru8in.pddhelper.downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.ConnectException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

suspend fun downloadFile(fileUrl: URL, outputPath: String) {
    withContext(Dispatchers.IO) {
        try {
            val input: InputStream = fileUrl.openStream()
            File(outputPath).mkdirs()
            Files.copy(input, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING)
        } catch (ex: ConnectException) {
            ex.printStackTrace()
        }
    }
}