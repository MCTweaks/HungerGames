plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.hetmastertje"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://papermc.io/repo/repository/maven-public/")

    // ITEM NBT API
    maven("https://repo.codemc.io/repository/maven-public/")
    // VaultAPI
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.4")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.11.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
    implementation("io.papermc:paperlib:1.0.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {

    compileJava{
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("hungerGames.jar")
        relocate("io.papermc.lib", "hungergames.paperlib")
    }
}