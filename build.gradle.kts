plugins {

    id("org.jetbrains.kotlin.android") version "1.9.10" apply false // Updated to 1.9.10
    id("org.jetbrains.dokka") version "1.9.10" apply false

}

buildscript {

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://chaquo.com/maven")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0") //Restored to correct version.
        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version") //removed.
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        //classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.30") removed outdated version.

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}