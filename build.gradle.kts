plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

kotlin {
    target {}
    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":statistics"))
                implementation(project(":downloader"))
            }
        }
    }
}