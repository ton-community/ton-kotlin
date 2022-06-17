kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonLiteClient)
                api(projects.tonBoc)
                api(projects.tonCrypto)
                api(libs.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
