import com.flixclusive.gradle.FlixclusiveExtension
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        // Shitpack which still contains some Flixclusive dependencies for now.
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        // Flixclusive gradle plugin which makes everything work and builds plugins
        classpath("com.github.Flixclusive.plugins-gradle:plugins-gradle:main-SNAPSHOT")
        // Kotlin support. Remove if you want to use Java
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

fun Project.flixclusive(configuration: FlixclusiveExtension.() -> Unit) = extensions.getByName<FlixclusiveExtension>("flixclusive").configuration()

fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "com.flixclusive.gradle")
    // Remove if using Java
    apply(plugin = "kotlin-android")

    // Fill out with your info
    flixclusive {
        // Add authors
        author(
            name = "MyUsername",
            // userLink = "http://github.com/myGithubUsername",
            // discordId = 123456789L
        )
        // author( ... )
        // author( ... )

        updateUrl.set("https://raw.githubusercontent.com/USERNAME/REPONAME/builds/updater.json")
        buildUrl.set("https://raw.githubusercontent.com/USERNAME/REPONAME/builds/%s.flx")
    }

    android {
        compileSdkVersion(31)

        buildFeatures.apply {
            compose = true
        }

        composeOptions.apply {
            kotlinCompilerExtensionVersion = "1.5.9"
        }

        defaultConfig {
            minSdk = 24
            targetSdk = 31
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11" // Required
                // Disables some unnecessary features
                freeCompilerArgs = freeCompilerArgs +
                        "-Xno-call-assertions" +
                        "-Xno-param-assertions" +
                        "-Xno-receiver-assertions"
            }
        }
    }

    dependencies {
        val flixclusive by configurations
        val implementation by configurations

        // Stubs for all Flixclusive classes
        flixclusive("com.flixclusive:flixclusive:pre-release")

        // Uncomment if implementing own SettingsScreen
        val composeBom = platform("androidx.compose:compose-bom:2024.01.00")
        implementation(composeBom)
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.foundation:foundation")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.runtime:runtime")
        // ================= END: COMPOSE UI =================

        // ============= START: SCRAPING TOOLS =============
        val okHttpBom = platform("com.squareup.okhttp3:okhttp-bom:4.12.0")
        implementation(okHttpBom)
        // define any required OkHttp artifacts without version
        implementation("com.squareup.okhttp3:okhttp")
        implementation("com.squareup.okhttp3:okhttp-dnsoverhttps")
        implementation("com.squareup.okhttp3:logging-interceptor")


        implementation("org.jsoup:jsoup:1.16.1")
        implementation("com.google.code.gson:gson:2.10.1")
        // ============== END: SCRAPING TOOLS =============

        // ============= START: FOR TESTING ===============
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        implementation("junit:junit:4.13.2")
        implementation("io.mockk:mockk:1.13.8")
        // ============== END: FOR TESTING ================
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
