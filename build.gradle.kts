plugins {
    alias(libs.plugins.kotlin)
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.test.junit)
    testImplementation(libs.test.strikt)
}