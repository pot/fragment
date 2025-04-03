plugins {
    java
    id("dev.nateweisz.fragment") version "1.0-SNAPSHOT"
}

dependencies {
    implementation("net.minestom:minestom-snapshots:620ebe5d6b")
}

fragment {
    packFile = file("./pack/pack.conf")
    outputFile = file("./build/pack.zip")
    shouldGenerateShifts = false
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

