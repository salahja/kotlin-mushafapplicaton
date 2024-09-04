// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // ext.kotlin_version = '1.1.60'
    val kotlin_version by extra("1.8.0")
    val nav_version by extra("2.4.1")
    val room_version by extra("2.4.2")
    val kotlin_plugin_noarg by extra("1.9.10")

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://chaquo.com/maven")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.30")
        //classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52")
    }
}

allprojects {
    repositories {
        google()

        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
