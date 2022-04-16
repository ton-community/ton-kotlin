kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonBlock)
                implementation(projects.tonHashmapTlb)
                implementation(projects.tonTlb)
            }
        }
        val commonTest by getting
    }
}