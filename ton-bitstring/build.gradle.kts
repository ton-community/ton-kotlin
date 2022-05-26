kotlin {
    sourceSets {
        commonMain {
            dependencies {
                compileOnly(libs.serialization.core)
                api(projects.tonCrypto)
            }
        }
        commonTest
    }
}
