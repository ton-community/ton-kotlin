import org.gradle.jvm.tasks.Jar
import java.net.URI

plugins {
    `maven-publish`
    signing
}

publishing {
    repositories {
        configureMavenPublication(project)
    }

    val javadocJar = project.configureEmptyJavadocArtifact()
    publications.withType(MavenPublication::class).all {
        pom.configureMavenCentralMetadata(project)
        signPublicationIfKeyPresent(project, this)
        artifact(javadocJar)
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
        dependsOn(tasks.withType<Sign>())
    }
}

// Pom configuration
infix fun <T> Property<T>.by(value: T) {
    set(value)
}

fun MavenPom.configureMavenCentralMetadata(project: Project) {
    name by project.name
    description by "A multiplatform Kotlin library providing basic cryptographic functions and primitives"
    url by "https://github.com/ton-community/ton-kotlin"

    licenses {
        license {
            name by "The Apache Software License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
            distribution by "repo"
        }
    }

    developers {
        developer {
            id by "andreypfau"
            name by "Andrey Pfau"
            email by "andreypfau@ton.org"
        }
    }

    scm {
        url by "https://github.com/ton-community/ton-kotlin"
    }
}

fun MavenPublication.mavenCentralArtifacts(project: Project, sources: SourceDirectorySet) {
    val sourcesJar by project.tasks.creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sources)
    }
    val javadocJar by project.tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        // contents are deliberately left empty
    }
    artifact(sourcesJar)
    artifact(javadocJar)
}


fun mavenRepositoryUri(): URI {
    val repositoryId: String? = System.getenv("SONATYPE_REPOSITORY_ID")
    return if (repositoryId == null) {
        URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
    } else {
        URI("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId")
    }
}

fun RepositoryHandler.configureMavenPublication(project: Project) {
    maven {
        url = mavenRepositoryUri()
        credentials {
            username = project.getSensitiveProperty("SONATYPE_USERNAME")
            password = project.getSensitiveProperty("SONATYPE_PASSWORD")
        }
    }

    mavenLocal()
}

fun Project.configureEmptyJavadocArtifact(): Jar {
    val javadocJar by project.tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        // contents are deliberately left empty
    }
    return javadocJar
}

fun signPublicationIfKeyPresent(project: Project, publication: MavenPublication) {
    val signingKey = project.getSensitiveProperty("SIGN_KEY_PRIVATE")
    val signingKeyPassphrase = project.getSensitiveProperty("SIGN_PASSPHRASE")
    if (!signingKey.isNullOrBlank()) {
        project.extensions.configure<SigningExtension>("signing") {
            useInMemoryPgpKeys(signingKey, signingKeyPassphrase)
            sign(publication)
        }
    }
}

fun Project.getSensitiveProperty(name: String): String? {
    return project.findProperty(name) as? String ?: System.getenv(name)
}
