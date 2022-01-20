plugins {
    kotlin("multiplatform")
}

allprojects {
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