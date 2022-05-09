kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonBigint)
                api(projects.tonCrypto)
                implementation(libs.ktor.utils)
                compileOnly(libs.serialization.json)
            }
        }
    }
}