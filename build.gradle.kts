plugins {
    java
    id("com.github.johnrengelman.shadow") version ("5.2.0")
}

group = "me.awesomemoder316.passwordlogon"
version = "1.1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:2.2.1")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
    relocate("org.bstats", "me.awesomemoder316.passwordlogon.dependencies")
}

