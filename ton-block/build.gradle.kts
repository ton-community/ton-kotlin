kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonBoc)
                api(projects.tonHashmap)
                api(projects.tonCrypto)
                api(projects.tonBigint)
                api(projects.tonTlb)
                implementation(libs.serialization.json)
            }
        }
        val commonTest by getting
    }
}
