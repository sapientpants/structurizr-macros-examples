import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.70"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.2.1"
}

group = "io.github.sapientpants"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.sapientpants.structurizr.macros.examples.BigBankPlcKt"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.sapientpants:structurizr-macros:0.0.34-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
}
