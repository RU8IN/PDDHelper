package com.ru8in.pddhelper.downloader

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

internal object FolderModifier{

    private fun renameDirectory(path: String, newName: String) {
        val file = File(path)
        file.renameTo(File("${file.getParent()}/$newName"))
    }

    suspend fun updateFolder(folderPath: String, keepZipArchive: Boolean = false) = coroutineScope {
        val folders = File(folderPath).listFiles { file -> file.isDirectory && file.name.contains("etspring-pdd_russia") }
        val originalFolder = folders?.firstOrNull()?.name
        launch {
            renameDirectory("$folderPath/$originalFolder", "tickets")
        }
        launch {
            if (!keepZipArchive) File("$folderPath/tickets.zip").delete()
            File("$folderPath/$originalFolder").deleteRecursively()
        }
    }

}