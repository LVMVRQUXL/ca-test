import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { kotlin("jvm") }

kotlin.explicitApi()

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotools.types)

    testImplementation(libs.kotlin.test)
}

tasks.withType<KotlinCompile>().configureEach {
    javaPackagePrefix = "lamarque.loic.catest.core"
    compilerOptions {
        allWarningsAsErrors.set(true)
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

tasks.test.configure(Test::useJUnitPlatform)
