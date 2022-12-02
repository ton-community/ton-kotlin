kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinLiteclient)
                api(libs.ktor.server.cio)
                api(libs.ktor.client.cio)
            }
        }
    }
}
