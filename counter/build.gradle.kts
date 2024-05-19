plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

version = "0.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.retry:spring-retry:2.0.5")
    implementation("org.springframework:spring-aspects:6.1.6")
    implementation("org.liquibase:liquibase-core:4.25.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    runtimeOnly("org.postgresql:postgresql")
}
