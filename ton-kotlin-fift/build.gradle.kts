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
                implementation(projects.tonKotlinLogger)
                implementation(projects.tonKotlinBigint)
                implementation(projects.tonKotlinBoc)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
