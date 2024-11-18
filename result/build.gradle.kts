
plugins {
    kotlin("jvm") version libs.versions.kotlinVersion
    `maven-publish`
    signing
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.test.junit)
    testImplementation(libs.test.strikt)
}