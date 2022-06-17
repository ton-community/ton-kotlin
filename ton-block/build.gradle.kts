kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonCell)
                api(projects.tonBoc)
                api(projects.tonHashmap)
                api(projects.tonCrypto)
                api(projects.tonBigint)
                api(projects.tonTlb)
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")
            }
        }
        val commonTest by getting
    }
}
