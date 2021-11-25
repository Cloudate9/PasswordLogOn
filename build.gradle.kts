plugins {
    id("com.github.johnrengelman.shadow") version("7.0.0")
    java
    id("kr.entree.spigradle") version ("2.2.4")
}

group = "io.github.awesomemoder316.passwordlogon"
version = "1.3.3"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:2.2.1")
    compileOnly("org.spigotmc:spigot-api:1.18-pre8-R0.1-SNAPSHOT")
}

tasks.compileJava {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    finalizedBy("bumpLatestVersionMd") //Make updater run all the time.
}

artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    minimize()
    archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
    relocate("org.bstats", "io.github.awesomemoder316.passwordlogon.dependencies")
}

spigot {
    authors = listOf("Awesomemoder316")
    apiVersion = "1.14"
    description = "Require players to use a password to log on!"
    website = "https://github.com/awesomemoder316/PasswordLogOn"

    commands {
        create("password") {
            aliases = listOf("pw")
            description = "A command to set and reset login password."
            usage = "/pw [set/reset/setarea]"
        }
    }
}

class BumpLatestVersionMd: Plugin<Project> {
    override fun apply(project: Project) {
        project.task("bumpLatestVersionMd") {
            val latestVersionMd = File(project.rootDir, "latestVersion.md")
            if (!latestVersionMd.exists()) latestVersionMd.createNewFile()
            doLast {
                val read = latestVersionMd.bufferedReader()
                val oldVersion = read.readLine() //Only one line expected.
                read.close()

                if (oldVersion != project.version) latestVersionMd.writeText(project.version.toString())
            }
        }
    }
}

apply<BumpLatestVersionMd>()
