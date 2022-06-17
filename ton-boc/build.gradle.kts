kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonCell)
                implementation(libs.ktor.utils)
                implementation(libs.serialization.json)
            }
        }
    }
}
