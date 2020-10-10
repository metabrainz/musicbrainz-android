// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    project.extra.apply {
        set("kotlinVersion", "1.4.0")
        set("navigationVersion", "2.3.0")
        set("archLifecycleVersion", "1.1.1")
        set("gradleVersion", "3.3.1")
        set("supportLibVersion", "1.1.0")
        set("lottieVersion", "3.4.0")
        set("lifecycleVersion", "2.3.0-beta01")
    }

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha13")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlinVersion"]}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}