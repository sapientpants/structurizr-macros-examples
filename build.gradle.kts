import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.41"
}

group = "io.github.sapientpants"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "io.sapientpants.structurizr.macros.example.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.sapientpants:structurizr-macros:0.0.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}