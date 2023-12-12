plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.serialization.json)
            }
        }
        nativeMain {
            dependencies {
                implementation(libs.bignum)
            }
        }
    }
}
