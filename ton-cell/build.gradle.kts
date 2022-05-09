kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonPrimitives)
                api(projects.tonCrypto)
                implementation(libs.ktor.utils)
                compileOnly(libs.serialization.json)
            }
        }
    }
}