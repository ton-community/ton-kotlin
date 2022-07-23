kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonLiteClient)
                api(projects.tonBoc)
                api(projects.tonCrypto)
                api(libs.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
