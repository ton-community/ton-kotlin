kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonCrypto)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
            }
        }
        val commonTest by getting
    }
}