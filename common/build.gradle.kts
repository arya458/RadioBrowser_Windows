import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "com.aria.danesh"
version = "1.0-SNAPSHOT"

kotlin {
    val koin_version = "3.2.0"



    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

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
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdkVersion(33)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(33)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}