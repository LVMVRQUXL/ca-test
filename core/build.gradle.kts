import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { kotlin("jvm") }

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotools.types)

    testImplementation(libs.kotlin.test)
}

tasks.withType<KotlinCompile>().configureEach {
    javaPackagePrefix = "lamarque.loic.catest.core"
    compilerOptions.allWarningsAsErrors.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)
