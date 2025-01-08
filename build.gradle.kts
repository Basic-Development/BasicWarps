import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("net.kyori.indra") version "3.1.3"
    id("net.kyori.indra.checkstyle") version "3.1.3"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.1.2"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    kotlin("jvm") version "1.7.21"
}

group = "com.generalsarcasam.basicwarps"
version = "1.0-SNAPSHOT"

indra {
    javaVersions {
        minimumToolchain(21)
        target(21)
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.21.3-R0.1-SNAPSHOT")

    implementation("cloud.commandframework", "cloud-paper", "1.8.4")
    implementation("cloud.commandframework", "cloud-minecraft-extras", "1.8.4")
    implementation("net.kyori:adventure-text-serializer-gson:4.14.0")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate("net.kyori:adventure-text-serializer-gson", "com.generalsarcasam.basicwarps.libs")

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
    }

    runServer {
        minecraftVersion("1.21.3")

        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            hangar("ViaVersion", "5.2.1")
            hangar("ViaBackwards", "5.2.1");
        }
    }
}

paperPluginYaml {
    main = "com.generalsarcasam.basicwarps.BasicWarps"
    authors = listOf("GeneralSarcasam")
    apiVersion = "1.21"

    dependencies {
        server("Vault", Load.BEFORE, required = true)
    }
}