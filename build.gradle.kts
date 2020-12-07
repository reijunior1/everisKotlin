import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val springBootVersion: String by project
val springCloudVersion: String by project
val springOpenApiVersion: String by project

plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.jpa") version "1.3.61"
    kotlin("plugin.spring") version "1.4.10"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://repo.gradle.org/gradle/libs-releases")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // BluPay
    implementation("br.com.blupay:blu-base-modules:1.0-SNAPSHOT")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Third-Party
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("io.projectreactor.addons:reactor-extra:3.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
