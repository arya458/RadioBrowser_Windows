import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

kotlin {
    jvm("desktop")
    sourceSets {
        val koin_version = "4.0.3"

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("ch.qos.logback:logback-classic:1.4.11")
                implementation("org.slf4j:slf4j-api:2.0.9")
                implementation("io.ktor:ktor-client-core:2.3.7")
                implementation("io.ktor:ktor-client-cio:2.3.7")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.slf4j:slf4j-simple:2.0.9")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")

            // Koin
            implementation(platform("io.insert-koin:koin-bom:$koin_version"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-core-coroutines")
            implementation("io.insert-koin:koin-compose")
            implementation("io.insert-koin:koin-compose-viewmodel")
            implementation("io.insert-koin:koin-logger-slf4j")

            // Ktor for HTTP requests
            implementation("io.ktor:ktor-client-core:2.3.8")
            implementation("io.ktor:ktor-client-cio:2.3.8")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")

            // Kotlinx Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

            api("io.github.qdsfdhvh:image-loader:1.7.8")
            api("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.7.8")
        }
    }
}


compose.desktop {
    application {
        mainClass = "main.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/icon.ico"))
                shortcut = true
                copyright ="Copyright 2025 Aria Danesh"
                description="A modern desktop application for browsing and streaming radio stations, built with Kotlin Multiplatform and Compose Desktop."
            }
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            includeAllModules = true
            packageName = "RadioBrowser"
            packageVersion = "1.0.4"
        }
    }
}
