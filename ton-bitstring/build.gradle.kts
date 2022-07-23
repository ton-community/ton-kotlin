kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(libs.serialization.core)
                api(projects.tonCrypto)
            }
        }
        val commonTest by getting {

        }
    }
}
