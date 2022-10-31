kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinCrypto)
                api(projects.tonKotlinTl)
                api(projects.tonKotlinBlock)
                api(projects.tonKotlinLiteclient)
                api(libs.serialization.json)
                api(libs.coroutines.core)
                api(libs.ktor.server.cio)
                api("io.github.reactivecircus.cache4k:cache4k:0.8.0")
            }
        }
    }
}
