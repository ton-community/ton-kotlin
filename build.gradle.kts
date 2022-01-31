plugins {
    kotlin("multiplatform")
    `maven-publish`
}

allprojects {
    group = "com.github.andreypfau"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting
    }
}