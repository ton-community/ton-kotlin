kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.serialization.json)
                implementation("com.ionspin.kotlin:bignum:0.3.7")
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
