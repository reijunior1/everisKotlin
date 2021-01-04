import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// General ~/.gradle/gradle.properties
val codeArtifactUri: String by project
val codeArtifactUsername: String by project
val codeArtifactToken: String by project

// Project ./gradle.properties
val kotlinVersion: String by project
val springBootVersion: String by project
val springCloudVersion: String by project
val springOpenApiVersion: String by project
val dbAddr: String by project
val dbName: String by project
val dbUser: String by project
val dbPass: String by project

plugins {
    id("java-library")
    id("maven-publish")
    id("org.flywaydb.flyway") version "7.3.2"
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.jpa") version "1.3.61"
    kotlin("plugin.spring") version "1.4.10"
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

flyway {
    url = "jdbc:postgresql://${dbAddr}/${dbName}"
    user = dbUser
    password = dbPass
    locations = arrayOf("classpath:db/migration")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri(codeArtifactUri)
        credentials {
            username = codeArtifactUsername
            password = codeArtifactToken
        }
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

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

    // Spring Doc OpenApi
    implementation("org.springdoc:springdoc-openapi-kotlin:${springOpenApiVersion}")
    implementation("org.springdoc:springdoc-openapi-ui:${springOpenApiVersion}")

    // Third-Party
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("io.projectreactor.addons:reactor-extra:3.4.0")
    implementation("com.google.code.gson:gson:2.8.6")
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))
        }
    }
    repositories {
        // Publish aws repository
        maven {
            url = uri(codeArtifactUri)
            credentials {
                username = codeArtifactUsername
                password = codeArtifactToken
            }
        }
    }
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
