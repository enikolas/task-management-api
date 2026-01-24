plugins {
    java
    alias(libs.plugins.springDependencyManagement)
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
    implementation(project(":application"))

    implementation(libs.springBootActuator)
    implementation(libs.springBootWeb)
    testImplementation(libs.springBootActuatorTest)
    testImplementation(libs.springBootWebTest)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
