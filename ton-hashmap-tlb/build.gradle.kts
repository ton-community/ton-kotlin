kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonHashmap)
                implementation(projects.tonTlb)
            }
        }
        val commonTest by getting
    }
}