import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    application
}

group = "me.joreilly"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(projects.common)
    implementation(projects.composewindowtitle)
    implementation(libs.image.loader)
    implementation(libs.material3.desktop)
}

application {
    mainClass.set("MainKt")
}
