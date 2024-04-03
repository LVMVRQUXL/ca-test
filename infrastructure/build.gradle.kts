import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(project(":core"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}

tasks.withType<KotlinCompile>().configureEach {
    javaPackagePrefix = "lamarque.loic.catest.infrastructure"
    compilerOptions.allWarningsAsErrors.set(true)
}
