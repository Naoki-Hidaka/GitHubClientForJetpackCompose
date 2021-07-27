import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.gradle.api.JavaVersion

fun Project.androidExt(configure: Action<BaseAppModuleExtension>) {
    (this as ExtensionAware).extensions.configure("android", configure)
}

fun BaseAppModuleExtension.kotlinOptions(configure: Action<KotlinJvmOptions>) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)
}

fun Project.applyCommon(action: Action<BaseAppModuleExtension>) {
    applyCommonExt(action)
}

fun Project.applyCommon() {
    applyCommonExt()
}

private fun Project.applyCommonExt(action: Action<BaseAppModuleExtension>? = null) {
    androidExt {
        compileSdk = 30
        buildToolsVersion = "30.0.3"

        defaultConfig {
            minSdk = 24
            targetSdk = 30
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
        action?.execute(this)
    }
}