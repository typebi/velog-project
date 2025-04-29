plugins {
    kotlin("jvm") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.4"
    application
}

group = "com.typebi"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-streams:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("com.example.streaming.KafkaStreamsApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}