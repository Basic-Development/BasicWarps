plugins {
    id("net.kyori.indra") version "2.0.2"
    id("net.kyori.indra.checkstyle") version "2.0.2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.vestriamc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.20.2-R0.1-SNAPSHOT")
}

tasks {
    indra {
        javaVersions {
            target(17)
        }
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        expand(
            "version" to rootProject.version,
            "name" to project.name
        )

    }

    shadowJar {
        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
    }
}
