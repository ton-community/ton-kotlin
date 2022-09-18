# Kotlin/Multiplatform SDK for [The Open Network](https://ton.org)

[![Maven Central][maven-central-svg]][maven-central]
[![JitPack][jitpack-svg]][jitpack]
[![Based on TON][ton-svg]][ton]

## Documentation

https://github.com/andreypfau/ton-kotlin/wiki/TON-Kotlin-documentation

## `build.gradle.kts`

```kotlin
val version = "main-SNAPSHOT" // Get actual version on: https://jitpack.io/#andreypfau/ton-kotlin

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.andreypfau:ton-kotlin:$version")
}
```

<!-- Badges -->

[maven-central-svg]: https://img.shields.io/maven-central/v/org.ton/ton-kotlin

[maven-central]: https://mvnrepository.com/artifact/org.ton/ton-kotlin

[jitpack-svg]: https://jitpack.io/v/andreypfau/ton-kotlin.svg

[jitpack]: https://jitpack.io/#andreypfau/ton-kotlin

[ton-svg]: https://img.shields.io/badge/Based%20on-TON-blue

[ton]: https://ton.org
