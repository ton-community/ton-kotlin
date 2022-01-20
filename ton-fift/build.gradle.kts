plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.0.0-beta-1")
                implementation("io.github.gciatto:kt-math:0.4.0")
                implementation(projects.tonTypes)
            }
        }
        val commonTest by getting
    }
}