plugins {
    kotlin("jvm") version "2.1.21"
}

group = "pl.brightinventions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ai.koog:koog-agents:0.2.1")
    implementation("org.jetbrains.koog:koog-openai:0.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}