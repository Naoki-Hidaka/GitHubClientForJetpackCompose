plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "jp.dosukoi.githubclientforjetpackcompose"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0"
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
    }
}

dependencies {

    implementation(project(":ui:view"))
    implementation(project(":ui:viewModel"))
    implementation(project(":data:api"))
    implementation(project(":data:repository"))
    implementation(project(":domain:usecase"))
    implementation(project(":domain:entity"))
    implementation(project(":domain:repository"))


    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // Compose
    val composeVersion = "1.1.1"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.compiler:compiler:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.foundation:foundation-layout:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-util:$composeVersion")
    implementation("com.google.accompanist:accompanist-pager:0.23.1")

    // Glance
    val glanceVersion = "1.0.0-SNAPSHOT"
    implementation("androidx.glance:glance:$glanceVersion")
    implementation("androidx.glance:glance-appwidget:$glanceVersion")

    // Hilt
    val hiltVersion = "2.41"
    val hiltJetpackVersion = "1.0.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltJetpackVersion")

    // Lifecycle
    val lifecycleVersion = "2.4.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    // Navigation
    val navVersion = "2.4.1"
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")

    // Coroutine
    val coroutineVersion = "1.6.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // OkHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Gson
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room
    val roomVersion = "2.4.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    val mockkVersion = "1.13.4"
    implementation("io.mockk:mockk:$mockkVersion")
    testImplementation("androidx.test.ext:junit-ktx:1.1.3")


    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")
    testImplementation("androidx.test:runner:1.4.0")
}
