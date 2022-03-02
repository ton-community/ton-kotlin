kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonBitstring)
                implementation("io.ktor:ktor-utils:2.0.0-beta-1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.3.2")
            }
        }
    }
}