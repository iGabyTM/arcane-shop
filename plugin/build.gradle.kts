plugins {
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

repositories {
    maven("https://repo.triumphteam.dev/snapshots")
    maven("https://jitpack.io")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    implementation(project(":api"))
    implementation(libs.bundles.adventure)
    implementation(libs.bundles.configurate)
    implementation(libs.bundles.triumph)
    implementation("de.tr7zw:item-nbt-api-plugin:2.10.0")

    compileOnly(libs.jetbrains.annotations)

    // Economy
    compileOnly(libs.vaultapi)
}

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    @Suppress("SpellCheckingInspection")
    shadowJar {
        archiveFileName.set("ArcaneShop [${project.version}].jar")

        mapOf(
            "org.spongepowered" to "spongepowered",
            "com.google.gson" to "google.gson",
            "net.kyori" to "kyori",
            "dev.triumphteam" to "triumphteam",
            "org.yaml" to "yaml"
        ).forEach { (key, value) -> relocate(key, "me.gabytm.minecraft.arcaneshop.libs.$value") }
    }

    task<Copy>("buildJar") {
        group = "build"
        from(shadowJar)
        into("../server/plugins")
    }
}