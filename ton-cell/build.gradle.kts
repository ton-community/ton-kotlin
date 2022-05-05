kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonBitstring)
                api(projects.tonPrimitives)
                api(projects.tonCrypto)
                implementation("io.ktor:ktor-utils:2.0.0-beta-1")
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }
}