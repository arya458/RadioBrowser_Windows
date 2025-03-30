import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization") version "1.9.0"
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
    
    sourceSets {
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
        val koin_version = "4.0.3"

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
            
            api("io.github.qdsfdhvh:image-loader:1.7.8")
            api("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.7.8")
        }
    }
}

compose.desktop {
    application {
        mainClass = "main.MainKt"
        jvmArgs += listOf("-Xmx2G")
        buildTypes {
            release {
                proguard {
                    configurationFiles.from("compose-desktop.pro")
                }
            }
        }
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "RadioBrowser"
            packageVersion = "1.0.0"
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/icon.ico"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
