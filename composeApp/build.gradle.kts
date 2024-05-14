import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation( "de.sfuhrm:radiobrowser4j:3.0.0")
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")
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
            includeAllModules = true
            packageName = "RadioBrowser"
            packageVersion = "1.0.0"
        }
    }
}
