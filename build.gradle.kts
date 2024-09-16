import com.android.build.gradle.BaseExtension
import com.flixclusive.gradle.FlixclusiveProviderExtension


buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        mavenLocal() // <- For testing
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        // Flixclusive gradle plugin which makes everything work and builds providers
        classpath("com.github.flixclusiveorg.core-gradle:core-gradle:1.1.6")
        // Kotlin support. Remove if you want to use Java
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

fun Project.flxProvider(configuration: FlixclusiveProviderExtension.() -> Unit) = extensions.getByName<FlixclusiveProviderExtension>("flxProvider").configuration()

fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "com.flixclusive.gradle")
    // Remove if using Java
    apply(plugin = "kotlin-android")

    // Fill out with your info
    flxProvider {
        /**
         *
         * Add the author(s) of this repository.
         *
         * Optionally, you can add your
         * own github profile link
         * */
        author(
            name = "flixclusiveorg",
            image = "http://github.com/flixclusiveorg.png",
            socialLink = "http://github.com/flixclusiveorg",
        )
        // author( ... )

        setRepository("https://github.com/flixclusiveorg/providers-template")
    }

    android {
        compileSdkVersion(34)

        namespace = "com.github.flixclusiveorg.providersTemplate.${name.replaceFirstChar { it.lowercase() }}"

        defaultConfig {
            minSdk = 21
            targetSdk = 34
        }

        // REQUIRED: BuildConfig must always be ON!
        buildFeatures.buildConfig = true

        compileOptions {
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString() // Required
            }
        }
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        val coreLibraryDesugaring by configurations

        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

        // Stubs for all Flixclusive classes
        implementation("com.github.flixclusiveorg.core-stubs:provider:1.0.4")

        // ============= START: FOR TESTING ===============
        testImplementation("com.github.flixclusiveorg.core-stubs:provider:1.0.4")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("junit:junit:4.13.2")
        testImplementation("io.mockk:mockk:1.13.8")
        // ============== END: FOR TESTING ================
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}