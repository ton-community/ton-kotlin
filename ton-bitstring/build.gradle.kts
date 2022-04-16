kotlin {
    sourceSets {
        commonMain {
            dependencies {
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
            }
        }
    }

    js {
        nodejs()
        useCommonJs()
        binaries.executable()
    }
}