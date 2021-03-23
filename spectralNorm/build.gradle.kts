import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_16
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_16
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("--enable-preview")
    compilerArgs.add("--add-modules=jdk.incubator.vector")
    compilerArgs.add("--add-modules=jdk.incubator.foreign")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("spectralNorm")
        mergeServiceFiles()
        isZip64 = true // needed for hive-jdbc, do not enable if not needed
        manifest {
            attributes(mapOf("Main-Class" to "SpectralNorm"))
        }
    }
}
tasks {
    build {
        dependsOn(shadowJar)
    }
}