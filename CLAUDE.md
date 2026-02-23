# CLAUDE.md — Agent Steering

## Project Overview

This is a GitHub template repository for building Java CLI applications with GraalVM native-image compilation. Users click "Use this template" on GitHub, clone the result, and immediately have a working CLI that compiles to a native executable.

## Tech Stack

- **Java 25** (LTS, language level 25)
- **Gradle 9.x** with Kotlin DSL and version catalogs (`gradle/libs.versions.toml`)
- **GraalVM native-image** via `native-build-tools` plugin
- **Picocli** for CLI framework with `picocli-codegen` annotation processor
- **JLine 3** for terminal handling (completion, history, colors)
- **JUnit 6** + **AssertJ** for testing
- **JSpecify** for null safety (`@NullMarked` at package level)
- **Google Java Format** via Spotless plugin (2-space indent)
- **Error Prone** for compile-time static analysis
- **JReleaser** for releases (Homebrew tap, GitHub Releases)

## Build Commands

```bash
gradle build              # Compile + test (JVM mode)
gradle nativeCompile      # Build native binary
gradle spotlessApply      # Auto-format code
gradle spotlessCheck      # Check formatting (CI)
```

## Versioning

- CalVer format: `vYYYY.MM.DD` derived from git tags (v prefix stripped for version number)
- No tag → version is `dev`
- No SNAPSHOT workflow — just tag and push

## Code Conventions

- All packages use `@NullMarked` (JSpecify) in `package-info.java`
- Use `@Nullable` only where nulls are explicitly intended
- Google Java Format: 2-space indent
- Conventional commits: `feat:`, `fix:`, `build:`, `ci:`, `docs:`, `refactor:`, `test:`
- Tests use AssertJ fluent assertions (`assertThat(...)`)
- Tests are committed alongside their feature in the same commit

## Project Structure

```
src/main/java/com/example/cli/
├── package-info.java          # @NullMarked
├── App.java                   # Root @Command, main()
├── VersionProvider.java       # Reads version.properties from classpath
└── commands/
    ├── package-info.java      # @NullMarked
    └── GreetCommand.java      # Example subcommand

src/test/java/com/example/cli/
├── AppTest.java
└── commands/
    └── GreetCommandTest.java
```

## Adding a New Subcommand

1. Create `src/main/java/com/example/cli/commands/MyCommand.java`
2. Annotate with `@Command(name = "mycommand", ...)`
3. Implement `Callable<Integer>`
4. Register in `App.java` via `subcommands = { ..., MyCommand.class }`
5. Write tests in `src/test/java/com/example/cli/commands/MyCommandTest.java`
