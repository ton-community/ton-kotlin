kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonCell)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
            }
        }
    }
}
