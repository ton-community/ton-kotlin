kotlin {
    sourceSets {
        commonMain {
            dependencies {
                compileOnly(libs.serialization.core)
            }
        }
        commonTest
    }
}