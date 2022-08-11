kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(libs.serialization.core)
                api(projects.tonKotlinCrypto)
            }
        }
        val commonTest by getting {

        }
    }
}
