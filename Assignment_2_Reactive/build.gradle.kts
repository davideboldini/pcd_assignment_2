plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.freva:ascii-table:1.8.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation("com.google.guava:guava:31.1-jre")

}

tasks.test {
    useJUnitPlatform()
}