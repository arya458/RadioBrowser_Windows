import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.aria.danesh"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                implementation("de.sfuhrm:radiobrowser4j:3.0.0")
                implementation("com.darkrockstudios:mpfilepicker:3.1.0")

                implementation("io.insert-koin:koin-core:$koin_version")
                api("io.github.qdsfdhvh:image-loader:1.7.8")
                api("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.7.8")
//            implementation("io.insert-koin:koin-core-coroutines:$koin_version")
//            implementation("io.insert-koin:koin-compose:$koin_version")
                implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha03")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "RadioBrowser"
            packageVersion = "1.0.0"
        }
    }
}
