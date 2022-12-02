kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinLiteclient)
                api(libs.ktor.server.cio)
                api(libs.ktor.client.cio)
                api(libs.serialization.json)
                api(libs.atomicfu)
                api("io.github.reactivecircus.cache4k:cache4k:0.8.0")
            }
        }
    }
}
