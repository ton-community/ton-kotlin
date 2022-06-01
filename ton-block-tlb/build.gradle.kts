kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonBlock)
                implementation(projects.tonHashmap)
                implementation(projects.tonTlb)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.tonBoc)
            }
        }
    }
}
