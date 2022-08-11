kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinCell)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
            }
        }
    }
}
