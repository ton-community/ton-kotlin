plugins {
    id("buildsrc.convention.multiplatform")
}

dependencies {
    commonMainApi(projects.tonKotlinTl)
    commonMainApi(projects.tonKotlinTonapiTl)
    commonMainApi(projects.tonKotlinBlockTlb) // TODO: remove, used in LiteServerRunSmcMethod
}

