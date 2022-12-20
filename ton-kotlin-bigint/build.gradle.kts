kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonKotlinCrypto)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting
    }
}
