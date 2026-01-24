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

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":application"))

    implementation(libs.springBootDataJpa)
    implementation(libs.springBootFlyway)
    implementation(libs.flywayPostgresql)
    compileOnly(libs.lombok)
    runtimeOnly(libs.postgreSql)
    annotationProcessor(libs.lombok)
    testImplementation(libs.springBootDataJpaTest)
    testImplementation(libs.springBootFlywayTest)
    testImplementation(libs.springBootTestContainers)
    testImplementation(libs.testContainersJunit)
    testImplementation(libs.testContainersPostgreSql)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
