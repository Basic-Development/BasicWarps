import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("net.kyori.indra") version "4.0.0"
    id("net.kyori.indra.checkstyle") version "4.0.0"
    id("com.gradleup.shadow") version "9.4.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    kotlin("jvm") version "2.4.0"
}

group = "com.generalsarcasam.basicwarps"
version = "1.1-RELEASE"

indra {
    javaVersions {
        minimumToolchain(25)
        target(25)
    }
}

repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/") // New Paper API endpoint
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.essentialsx.net/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.74-stable")

    // libraries
    implementation("net.kyori:adventure-text-serializer-gson:5.2.0")
    implementation("org.incendo:cloud-paper:2.1.0-SNAPSHOT")
    implementation("org.incendo:cloud-minecraft-extras:2.1.0-SNAPSHOT")
    implementation("org.incendo:cloud-processors-confirmation:1.0.0-rc.1")

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
        minecraftVersion("26.1.2")

        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            github("EssentialsX", "Essentials", "2.22.0", "EssentialsX-2.22.0.jar")
            hangar("ViaVersion", "5.11.0")
            hangar("ViaBackwards", "5.11.0")

        }
    }
}

paperPluginYaml {
    main = "com.generalsarcasam.basicwarps.BasicWarps"
    authors = listOf("GeneralSarcasam")
    apiVersion = "26.1.2"

    //dependencies {}
}