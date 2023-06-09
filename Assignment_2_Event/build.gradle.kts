plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.github.freva:ascii-table:1.8.0")
    implementation("io.vertx:vertx-core:4.4.1")
    implementation("io.vertx:vertx-web:4.4.1")
    implementation("io.vertx:vertx-web-client:4.4.1")
    implementation("io.vertx:vertx-hazelcast:4.4.1")
    implementation("com.google.guava:guava:31.1-jre")
}

tasks.test {
    useJUnitPlatform()
}