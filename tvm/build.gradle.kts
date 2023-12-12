plugins {
    id("multiplatform")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonKotlinBitstring)
                api(projects.tonKotlinBigint)
                api(projects.tonKotlinCrypto)
                implementation(libs.serialization.json)
            }
        }
    }
}
