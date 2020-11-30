import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "ru.tinkoff.delivery.state.spring"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

apply(plugin = "io.spring.dependency-management")

repositories {
	mavenCentral()
}

dependencies {
    //implementation(platform("org.springframework.statemachine:spring-statemachine-bom:2.1.0.RC1"))
    //implementation("org.springframework.statemachine:spring-statemachine-bom:2.1.0.RC1:pom")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("io.github.microutils:kotlin-logging:2.0.3")
	implementation("org.springframework.statemachine:spring-statemachine-data-mongodb:2.2.0.RELEASE")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.statemachine:spring-statemachine-starter:2.2.0.RELEASE")
    implementation("org.testcontainers:testcontainers:1.14.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
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
