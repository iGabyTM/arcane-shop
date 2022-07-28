plugins {
    id("java")
    id("version-catalog")
}

group = "me.gabytm.minecraft"
version = "1.0-SNAPSHOT"

subprojects {

    apply(plugin = "java")
    apply(plugin = "version-catalog")

    this.group = group
    this.version = version

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    }

}