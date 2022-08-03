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
    implementation(libs.bundles.triumph)

    compileOnly(libs.jetbrains.annotations)

    // Economy
    compileOnly(libs.vaultapi)
}

tasks {
    processResources {
        eachFile { expand("version" to project.version) }
    }

    shadowJar {
        archiveFileName.set("ArcaneShop [${project.version}].jar")

        relocate("net.kyori", "me.gabytm.minecraft.arcaneshop.libs.kyori")
        relocate("dev.triumphteam", "me.gabytm.minecraft.arcaneshop.libs.triumph")
    }

    task<Copy>("buildJar") {
        group = "build"
        from(shadowJar)
        into("../server/plugins")
    }
}