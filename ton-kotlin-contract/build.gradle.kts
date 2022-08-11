kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinLiteclient)
                api(projects.tonKotlinBoc)
                api(projects.tonKotlinCrypto)
                api(libs.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
