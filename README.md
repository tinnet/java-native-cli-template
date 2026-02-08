# java-native-cli-template

A GitHub template for building Java CLI applications that compile to native executables via GraalVM native-image.

Click **"Use this template"** on GitHub, clone your new repo, and you're ready to go.

## Quick Start

### Prerequisites

Install [mise](https://mise.jdx.dev/) (or manually install Java 25 with GraalVM and Gradle 9.x):

```bash
mise install        # installs Java 25 (GraalVM) + Gradle 9.3.1
```

### Build & Run

```bash
gradle build              # compile + test (JVM mode, fast)
gradle nativeCompile      # build native binary (~30s)

./build/native/nativeCompile/mycli --help
./build/native/nativeCompile/mycli --version
./build/native/nativeCompile/mycli greet --name Ada
```

### Developer Commands

```bash
gradle spotlessApply      # auto-format code (Palantir Java Format)
gradle spotlessCheck      # check formatting (used in CI)
gradle test               # run tests only
```

## What's Included

| Component | What | Version |
|-----------|------|---------|
| Runtime | Java 25 (LTS) + GraalVM native-image | 25 |
| CLI | [Picocli](https://picocli.info/) + codegen | 4.7.7 |
| Terminal | [JLine 3](https://github.com/jline/jline3) | 3.30.6 |
| Null safety | [JSpecify](https://jspecify.dev/) `@NullMarked` | 1.0.0 |
| Testing | JUnit 6 + [AssertJ](https://assertj.github.io/doc/) | 6.0.2 / 3.27.7 |
| Formatting | Palantir Java Format via [Spotless](https://github.com/diffplug/spotless) | 8.2.0 |
| Static analysis | [Error Prone](https://errorprone.info/) | 2.46.0 |
| Build | Gradle (Kotlin DSL) | 9.3.1 |
| Release | [JReleaser](https://jreleaser.org/) | 1.22.0 |
| CI/CD | GitHub Actions | - |

### Commented-Out Libraries

These are pre-configured in `gradle/libs.versions.toml` — just uncomment to use:

- **Jackson** (JSON/YAML serialization)
- **Apache Commons Lang3** (string utils, reflection helpers)
- **Apache Commons IO** (file/stream utilities)
- **SLF4J + Logback** (logging)
- **Lanterna** (full terminal UI with windows, dialogs, tables)

## Customization Checklist

After clicking "Use this template":

1. **Binary name** - rename `rootProject.name` in `settings.gradle.kts`
2. **Package** - rename `com.example.cli` to your own package
3. **Main class** - update `mainClass` in `build.gradle.kts`
4. **Command name** - update `@Command(name = ...)` in `App.java`
5. **JReleaser** - update `jreleaser.yml` with your GitHub owner/repo
6. **Example command** - delete `GreetCommand.java` and add your own

## Adding a Subcommand

```java
package com.example.cli.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "mycommand", mixinStandardHelpOptions = true,
         description = "Does something useful.")
public class MyCommand implements Callable<Integer> {

    @Option(names = {"-f", "--flag"}, description = "A flag.")
    private boolean flag;

    @Override
    public Integer call() {
        // your logic here
        return 0;
    }
}
```

Then register it in `App.java`:

```java
@Command(..., subcommands = { MyCommand.class })
```

## Versioning

This template uses **CalVer** (calendar versioning) derived from git tags:

- Tag format: `vYYYY.MM.DD` (e.g., `v2025.06.15`)
- Same-day releases: `v2025.06.15.1`, `v2025.06.15.2`, etc.
- No tag = `dev` version (local development)
- No SNAPSHOT workflow

```bash
git tag v2025.06.15
git push --tags       # triggers release workflow
```

## Releasing

When you push a CalVer tag, GitHub Actions will:

1. Build native binaries on Linux, macOS (arm64 + x86_64), and Windows
2. Create a GitHub Release with the binaries
3. Update your Homebrew tap (configure in `jreleaser.yml`)

### Homebrew Setup

1. Create a `homebrew-tap` repository on your GitHub account
2. Update `jreleaser.yml` with your GitHub owner
3. Push a tag to trigger the release

### aqua / mise

Register your tool with the [aqua registry](https://github.com/aquaproj/aqua-registry) by submitting a PR. Since mise reimplements aqua's registry, your tool will be installable via both.

## Project Structure

```
src/main/java/com/example/cli/
├── package-info.java          # @NullMarked (JSpecify)
├── App.java                   # Root @Command, main()
├── VersionProvider.java       # Reads version.properties
└── commands/
    ├── package-info.java      # @NullMarked
    └── GreetCommand.java      # Example subcommand

src/test/java/com/example/cli/
├── AppTest.java
└── commands/
    └── GreetCommandTest.java
```

## License

[MIT](LICENSE)
