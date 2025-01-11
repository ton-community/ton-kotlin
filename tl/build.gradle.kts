plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinCrypto)
    commonMainApi(projects.tonKotlinCore)
    commonMainApi(libs.serialization.core)
    commonMainApi(libs.serialization.json)

    commonTestApi(libs.ktor.utils)
}

//kotlin {
//    sourceSets {
//        commonMain {
//            dependencies {
//                api(projects.tonKotlinCrypto)
//                api(projects.tonKotlinBitstring)
//                api(libs.ktor.utils)
//                api(libs.serialization.json)
//                api(libs.kotlinx.io)
//            }
//        }
//    }
//}
