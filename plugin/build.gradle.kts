plugins {
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

repositories {
    maven("https://repo.triumphteam.dev/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":api"))
    implementation(libs.bundles.adventure)
    implementation(libs.bundles.configurate)
    implementation(libs.bundles.triumph)

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
            "com.google" to "google",
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