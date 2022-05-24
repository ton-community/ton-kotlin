kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonCrypto)
            }
        }
        val commonTest by getting
    }
}
