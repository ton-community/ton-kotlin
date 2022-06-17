kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonCrypto)
                compileOnly(libs.serialization.json)
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
            }
        }
        val commonTest by getting
    }
}