plugins {
    id("buildsrc.convention.multiplatform")
    id("buildsrc.convention.layout")
    id("buildsrc.convention.publish")
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
