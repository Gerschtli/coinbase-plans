import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version "0.38.0"
    id("com.google.cloud.tools.jib") version "2.8.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.boot") version "2.4.4"
    id("se.patrikerdes.use-latest-versions") version "0.2.15"
    kotlin("jvm") version "1.4.30"
    kotlin("kapt") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
}

group = "de.tobiashapp.coinbase"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_15
kapt.includeCompileClasspath = false

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.icegreen:greenmail-junit5:1.6.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")

    implementation(files("libs/coinbase-pro-java-0.11.0-all.jar"))

    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.0.0")
    testImplementation("org.awaitility:awaitility-kotlin:4.0.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "openjdk:15-alpine"
    }
    to {
        image = "docker.pkg.github.com/gerschtli/coinbase-plans/coinbase-plans:latest"
        auth {
            username = "Gerschtli"
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
