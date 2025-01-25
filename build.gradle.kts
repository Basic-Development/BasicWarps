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
    sonatype.ossSnapshots()
    maven("https://repo.papermc.io/repository/maven-public/") // New Paper API endpoint
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.essentialsx.net/releases/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.21.4-R0.1-SNAPSHOT")

    // libraries
    implementation("net.kyori:adventure-text-serializer-gson:4.18.0")
    implementation("org.incendo", "cloud-paper", "2.0.0-SNAPSHOT")
    implementation("org.incendo", "cloud-minecraft-extras", "2.0.0-SNAPSHOT")
    implementation("org.incendo", "cloud-processors-confirmation", "1.0.0-rc.1")

    // integrations
    implementation(kotlin("stdlib"))
}


tasks {
    assemble {
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate("org.incendo.cloud", "com.generalsarcasam.basicwarps.libs.cloud")

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
    }

    runServer {
        minecraftVersion("1.21.4")

        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            github("EssentialsX", "Essentials", "2.20.1", "EssentialsX-2.20.1.jar")
            hangar("ViaVersion", "5.2.1")
            hangar("ViaBackwards", "5.2.1")

        }
    }
}

paperPluginYaml {
    main = "com.generalsarcasam.basicwarps.BasicWarps"
    authors = listOf("GeneralSarcasam")
    apiVersion = "1.21.4"

    //dependencies {}
}