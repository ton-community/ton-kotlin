kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.tonLiteClient)
                api(projects.tonBoc)
                api(projects.tonCrypto)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
