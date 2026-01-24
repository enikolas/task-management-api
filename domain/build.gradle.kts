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
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
