kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonCrypto)
                compileOnly(libs.serialization.json)
            }
        }
        val commonTest by getting
    }
}