plugins {
    id("java")
}

group = "aliz.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.crypto.tink:tink:1.6.1")
    implementation("com.google.crypto.tink:tink-gcpkms:1.6.1")

    // we need this for now, see: https://github.com/google/tink/issues/549
    implementation("com.google.http-client:google-http-client-jackson2:1.40.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}