kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonHashmap)
                api(projects.tonTlb)
            }
        }
        val commonTest by getting
    }
}
