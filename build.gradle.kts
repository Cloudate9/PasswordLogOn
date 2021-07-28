plugins {
    java
    id("com.github.johnrengelman.shadow") version("7.0.0")
}

group = "me.awesomemoder316.passwordlogon"
version = "1.3.0"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:2.2.1")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

tasks.compileJava {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
    relocate("org.bstats", "me.awesomemoder316.passwordlogon.dependencies")
}

