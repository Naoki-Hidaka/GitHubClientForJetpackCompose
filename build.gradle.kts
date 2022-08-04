// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.5.10"

        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.1")

        val navVersion = "2.4.1"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")

        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()

        maven("https://androidx.dev/snapshots/builds/7957905/artifacts/repository")
    }

    val ktlint by configurations.creating
    dependencies {
        ktlint("com.pinterest:ktlint:0.44.0") {
            attributes {
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            }
        }
    }

    val outputDir = "${buildDir}/reports/ktlint/"
    val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

    val ktlintCheck by tasks.creating(JavaExec::class) {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.pinterest.ktlint.Main"
        args = listOf("src/**/*.kt")
    }

    val ktlintFormat by tasks.creating(JavaExec::class) {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.pinterest.ktlint.Main"
        args = listOf("-F", "src/**/*.kt")
    }
}

val projectDependencyGraph by tasks.registering(ProjectDependencyGraphTask::class) {
    doLast {
        copy {
            from(rootProject.buildDir.resolve("reports/dependency-graph/project.dot.png"))
            into(rootProject.projectDir)
        }
    }
}

