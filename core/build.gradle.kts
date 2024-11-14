plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "22"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(project(mapOf("path" to ":downloader")))
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
}
