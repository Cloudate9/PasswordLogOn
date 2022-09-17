import kr.entree.spigradle.kotlin.*

plugins {
    id("com.github.johnrengelman.shadow") version("7.1.2")
    id("kr.entree.spigradle") version ("2.4.2")
    java
}

group = "com.cloudate9.passwordlogon"
version = "2.0.0"

repositories {
    mavenCentral()
    papermc()
}

dependencies {
    compileOnly(paper("1.19.2"))
    implementation(bStats("3.0.0"))
}

tasks.compileJava {
    options.release.set(17)
}

tasks {
    prepareSpigotPlugins {
        setDependsOn(mutableListOf(shadowJar))
    }

    runSpigot {
        jvmArgs = mutableListOf(
            "-Xmx2G",
            "-Xms2G",
            "-XX:+UseZGC",
            "-XX:+ZUncommit",
            "-XX:ZUncommitDelay=3600",
            "-XX:+ZProactive",
            "-XX:+AlwaysPreTouch",
            "-XX:+DisableExplicitGC",
        )

        setDependsOn(mutableListOf(acceptSpigotEula, configSpigot, prepareSpigotPlugins))
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
        relocate("org.bstats", "com.cloudate9.passwordlogon.dependencies")
    }
}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.19"
    description = "Require players to use a password to log on!"
    excludeLibraries = listOf("*")
    website = "https://github.com/cloudate9/PasswordLogOn"

    commands {
        create("password") {
            aliases = listOf("pw")
            description = "A command to set and reset login password."
            usage = "/pw [login/set/reset/setarea]"
        }
    }

    permissions {
        create("passwordlogon.setarea") {
            description = "Allows you to set the area where players can log on."
        }
    }
}
