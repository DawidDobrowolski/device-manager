plugins {
    id("java")
    id("groovy")
    id("org.springframework.boot") version "3.3.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dd.task"
version = "0.0.1-SNAPSHOT"
description = "device-manager"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.liquibase:liquibase-core:4.29.2")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok:1.18.42")
    compileOnly("org.hibernate.orm:hibernate-jpamodelgen:6.6.1.Final")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.4.1.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.spockframework:spock-core:2.4-groovy-4.0")
    testImplementation("org.spockframework:spock-spring:2.4-groovy-4.0")
    testImplementation("com.h2database:h2")
    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
        options.compilerArgs.add("-Amapstruct.defaultInjectionStrategy=constructor")
    }

    withType<Test> {
        useJUnitPlatform()
    }

    bootJar {
        archiveClassifier.set("final")
    }
}
