import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.dokka")
    // id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 34
    buildToolsVersion = "33.0.1"

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://chaquo.com/maven")
        mavenCentral {
            content {
                includeModule("com.theartofdev.edmodo", "android-image-cropper")
            }
        }
    }

    defaultConfig {
        applicationId = "com.google.codelabs.mdc.java"
        applicationId = "com.com.salah.QuranEntity"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
                arguments["room.incremental"] = "true"
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

   /* compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }*/

    compileOptions {
        sourceCompatibility =JavaVersion.VERSION_17 // Or your desired Java version
                targetCompatibility =JavaVersion.VERSION_17 // Or your desired Java version
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }

    namespace = "com.example.mushafconsolidated"
}

dependencies {
    //implementation("androidx.paging:paging-runtime-ktx:3.x.x")
   // implementation ("com.thoughtbot:expandablerecyclerview:1.4")
    implementation("androidx.paging:paging-common-android:3.3.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    debugImplementation("androidx.compose.ui:ui-test-manifest:<version>")
    // new
    implementation ("com.google.code.gson:gson:2.11.0")

    implementation("com.github.amitshekhariitbhu:PRDownloader:1.0.1")
    implementation("io.coil-kt:coil-compose:2.7.0")
    // implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    // kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.compose.ui:ui-tooling:1.6.8")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.webkit:webkit:1.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.4")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0")
    implementation("androidx.sqlite:sqlite-ktx:2.4.0")
    implementation("androidx.window:window:1.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.20"))
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.readystatesoftware.sqliteasset:sqliteassethelper:2.0.1")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
    implementation("org.ahocorasick:ahocorasick:0.6.3")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.github.douglasjunior:android-simple-tooltip:1.1.0")
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.tukaani:xz:1.9")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("com.google.android.material:material:1.12.0")

}

kapt {
    correctErrorTypes = true
}
