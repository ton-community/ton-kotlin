kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.tonTlb)
                implementation(projects.tonCell)
                implementation(projects.tonCrypto)
            }
        }
    }

    js {
        browser()
        binaries.executable()
    }
    tasks.named<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>("compileKotlinJs").configure {
        kotlinOptions.moduleKind = "amd"
    }
}

