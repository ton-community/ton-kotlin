kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonKotlinCrypto)
                compileOnly(libs.serialization.json)
            }
        }
        val commonTest by getting
    }
}
