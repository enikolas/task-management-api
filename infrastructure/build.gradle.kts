plugins {
    java
    idea
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.enikolas.taskmanagement"
version = libs.versions.appVersion.get()
description =
    "Task Management API built with Java 25 and Spring Boot, following Clean Architecture principles with a focus on modularity, security, testing, and observability."

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


val integrationTest by sourceSets.creating

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}
val integrationTestRuntimeOnly by configurations.getting

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

idea {
    module {
        testSources.from(sourceSets["integrationTest"].java.srcDirs)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    implementation("org.flywaydb:flyway-database-postgresql")
    implementation(libs.springdoc)

    implementation("org.mindrot:jbcrypt:0.4")

    compileOnly(libs.lombok)
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor(libs.lombok)

    testImplementation(libs.assertJ)
    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.junitJupiterParams)
    testImplementation(libs.mockitoJunitJupiter)
    testRuntimeOnly(libs.junitJupiterEngine)
    testRuntimeOnly(libs.junitPlatformLauncher)

    integrationTestImplementation(project(":infrastructure"))
    integrationTestImplementation(libs.assertJ)
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    integrationTestImplementation("org.springframework.boot:spring-boot-testcontainers")
    integrationTestImplementation("org.testcontainers:testcontainers-junit-jupiter")
    integrationTestImplementation("org.testcontainers:testcontainers-postgresql")

    integrationTestImplementation(libs.junitJupiterApi)
    integrationTestImplementation(libs.junitJupiterParams)
    integrationTestRuntimeOnly(libs.junitJupiterEngine)
    integrationTestRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")

    useJUnitPlatform()
}

tasks.check {
    dependsOn("integrationTest")
}
