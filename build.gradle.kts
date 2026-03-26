import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "4.0.3"
}

group = "com.chesire"
version = "0.0.1-SNAPSHOT"
description = "Project to update multiple libraries at once"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.14.9")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    toolVersion = "1.23.8"
    source.setFrom("src/main/kotlin", "src/test/kotlin")
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    reports {
        sarif.required.set(true)
        sarif.outputLocation.set(file("build/reports/detekt.sarif"))
    }
}

dependencyManagement {
    configurations.matching { it.name == "detekt" }.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
            }
        }
    }
}
