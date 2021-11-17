plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    applyCommon()
}

dependencies {

    api(project(":data:repository"))
    testImplementation(project(":testing"))

    // Hilt
    val hiltVersion = "2.39.1"
    val hiltJetpackVersion = "1.0.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltJetpackVersion")

    // Coroutine
    val coroutineVersion = "1.5.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // OkHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.2"))

    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Lifecycle
    val lifecycleVersion = "2.3.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // Room
    val roomVersion = "2.3.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Test
    val mockkVersion = "1.12.1"
    implementation("io.mockk:mockk:$mockkVersion")
    testImplementation("androidx.test.ext:junit-ktx:1.1.3")


    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")
    testImplementation("androidx.test:runner:1.4.0")
}
