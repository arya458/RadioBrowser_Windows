import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        val koin_version = "3.2.0"

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation("de.sfuhrm:radiobrowser4j:3.0.0")
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")

            implementation("io.insert-koin:koin-core:$koin_version")
            api("io.github.qdsfdhvh:image-loader:1.7.8")
            api("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.7.8")
//            implementation("io.insert-koin:koin-core-coroutines:$koin_version")
//            implementation("io.insert-koin:koin-compose:$koin_version")
            implementation("io.insert-koin:koin-logger-slf4j:$koin_version")


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/icon.ico"))
                shortcut = true
            }
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            includeAllModules = true
            packageName = "RadioBrowser"
            packageVersion = "1.0.2"
        }
    }
}
