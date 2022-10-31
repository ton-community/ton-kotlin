kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinCrypto)
                api(projects.tonKotlinTl)
                api(projects.tonKotlinBlock)
                api(projects.tonKotlinLiteclient)
                implementation(libs.serialization.json)
                implementation(libs.coroutines.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.atomicfu)
                implementation("io.github.reactivecircus.cache4k:cache4k:0.8.0")
            }
        }
    }
}
