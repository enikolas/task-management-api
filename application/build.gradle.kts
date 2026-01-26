plugins {
    java
}

group = "io.github.enikolas.taskmanagement"
version = libs.versions.appVersion.get()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.junitJupiterParams)
    testImplementation(libs.mockitoJunitJupiter)
    testRuntimeOnly(libs.junitJupiterEngine)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
