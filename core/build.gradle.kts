plugins { kotlin("jvm") }

kotlin.explicitApi()

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotools.types)

    testImplementation(libs.kotlin.test)
}

tasks.test.configure(Test::useJUnitPlatform)
