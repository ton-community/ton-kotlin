kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonPrimitives)
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
            }
        }
        commonTest
    }
}