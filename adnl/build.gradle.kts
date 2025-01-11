plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTonapiTl)
    commonMainApi(libs.coroutines.core)
    commonMainApi(libs.datetime)
    commonMainApi(libs.ktor.network)
    commonMainApi(libs.atomicfu)
}

//plugins {
//    id("multiplatform")
//    id("publish")
//}
//
//kotlin {
//    sourceSets {
//        commonMain {
//            dependencies {
//                api(projects.tonKotlinTonapiTl)
//                implementation(libs.serialization.json)
//                implementation(libs.atomicfu)
//                implementation(libs.datetime)
//                implementation(libs.coroutines.core)
//                implementation(libs.ktor.utils)
//            }
//        }
//        nativeMain {
//            dependencies {
//                implementation(libs.bignum)
//            }
//        }
//        appleMain {
//            dependencies {
//                implementation(libs.ktor.network)
//            }
//        }
//        jvmMain {
//            dependencies {
//                implementation(libs.ktor.network)
//            }
//        }
//        linuxMain {
//            dependencies {
//                implementation(libs.ktor.network)
//            }
//        }
//    }
//}
