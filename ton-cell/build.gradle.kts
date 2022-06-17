kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonBigint)
                api(projects.tonCrypto)
                implementation(libs.serialization.json)
            }
        }
    }
}
