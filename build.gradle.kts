import java.time.Instant
import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    application
    alias(libs.plugins.graalvm.native)
    alias(libs.plugins.spotless)
    alias(libs.plugins.errorprone)
}

// ── CalVer from git tags (no SNAPSHOT dance) ────────────────────────────
val gitVersion: String by lazy {
    try {
        val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
            .directory(projectDir)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        if (process.waitFor() == 0 && output.isNotEmpty()) output else "dev"
    } catch (_: Exception) {
        "dev"
    }
}
version = gitVersion
group = "com.example"

// ── Java toolchain ──────────────────────────────────────────────────────
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

// ── Application ─────────────────────────────────────────────────────────
application {
    // Template users: update this to your main class
    mainClass.set("com.example.cli.App")
}

// ── Dependencies ────────────────────────────────────────────────────────
repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.picocli)
    annotationProcessor(libs.picocli.codegen)
    implementation(libs.jline)
    implementation(libs.jspecify)

    errorprone(libs.errorprone.core)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.assertj.core)

    // --- Optional libraries (uncomment in libs.versions.toml first) ---
    // implementation(libs.jackson.databind)
    // implementation(libs.jackson.dataformat.yaml)
    // implementation(libs.commons.lang3)
    // implementation(libs.commons.io)
    // implementation(libs.slf4j.api)
    // runtimeOnly(libs.logback.classic)
    // implementation(libs.lanterna)
}

// ── Picocli annotation processor config ─────────────────────────────────
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Aproject=${project.group}/${project.name}")
}

// ── Generate version.properties at build time ───────────────────────────
tasks.processResources {
    val buildTimestamp = Instant.now().toString()
    inputs.property("version", version)
    inputs.property("buildTimestamp", buildTimestamp)
    filesMatching("version.properties") {
        expand(
            "version" to version,
            "name" to project.name,
            "buildTimestamp" to buildTimestamp,
        )
    }
}

// ── Tests ────────────────────────────────────────────────────────────────
tasks.test {
    useJUnitPlatform()
}

// ── Spotless (Palantir Java Format) ─────────────────────────────────────
spotless {
    java {
        palantirJavaFormat()
        importOrder()
        removeUnusedImports()
    }
}

// ── Error Prone ─────────────────────────────────────────────────────────
tasks.withType<JavaCompile>().configureEach {
    options.errorprone.disableWarningsInGeneratedCode = true
}

// ── GraalVM Native Image ────────────────────────────────────────────────
graalvmNative {
    binaries {
        named("main") {
            imageName.set(project.name)
            mainClass.set("com.example.cli.App")
            buildArgs.addAll("--no-fallback", "-O2")
        }
    }
}
