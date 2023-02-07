kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.serialization.json)
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
