plugins {
    java
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

group = "io.github.enikolas.taskmanagement"
version = libs.versions.appVersion.get()
description = "Task Management API built with Java 25 and Spring Boot, following Clean Architecture principles with a focus on modularity, security, testing, and observability."

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":adapters-web"))
    implementation(project(":adapters-persistence-jpa"))

    implementation(libs.springdoc)

    testImplementation(libs.springBootStartTest)
    testImplementation(libs.springBootTestContainers)
    testImplementation(libs.testContainersJunit)
    testImplementation(libs.testContainersPostgreSql)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
