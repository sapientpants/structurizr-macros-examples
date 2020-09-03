import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.3.0"
}

group = "io.github.sapientpants"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.sapientpants.structurizr.macros.examples.BigBankPlcKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.sapientpants:structurizr-macros:2020.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
}
