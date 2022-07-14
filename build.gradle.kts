import kr.entree.spigradle.kotlin.bStats
import kr.entree.spigradle.kotlin.codemc
import kr.entree.spigradle.kotlin.jitpack
import kr.entree.spigradle.kotlin.papermc

plugins {
    id("com.github.johnrengelman.shadow") version("7.1.2")
    id("kr.entree.spigradle") version ("2.4.2")
    java
}

group = "io.github.cloudate9.passwordlogon"
version = "1.3.6"

repositories {
    mavenCentral()
    codemc()
    jitpack()
    papermc()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    //implementation("com.github.secretx33.sc-cfg:sccfg-bukkit:main-SNAPSHOT")
    //implementation("com.github.secretx33.sc-cfg:sccfg-yaml:main-SNAPSHOT")
    implementation(bStats("3.0.0"))
}

tasks.compileJava {
    options.release.set(17)
}

tasks.shadowJar {
    minimize()
    archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
    //relocate("com.github.secretx33.sccfg", "${rootProject.group}.dependencies.com.github.secretx33.sccfg")
    relocate("org.bstats", "io.github.cloudate9.passwordlogon.dependencies")
}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.14"
    //apiVersion = "1.18"
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
