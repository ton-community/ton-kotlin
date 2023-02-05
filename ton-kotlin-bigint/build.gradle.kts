kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.tonKotlinCrypto)
                implementation(libs.serialization.json)
                implementation(libs.atomicfu)
            }
        }
        val commonTest by getting
        val nativeMain by getting {
            dependencies {
                implementation("com.ionspin.kotlin:bignum:0.3.7")
            }
        }
    }
}
