kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonKotlinCrypto)
            }
        }
        val commonTest by getting
    }
}
