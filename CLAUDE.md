# CLAUDE.md

**⚠️ IMPORTANT**: If a `CLAUDE.local.md` file exists in this repository, it MUST be read first before proceeding with any work.

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Gattai** is a backend API service designed to act as a unified interface for updating library tracking information across multiple platforms simultaneously. For example, users can update their progress on MyAnimeList, AniList, and Kitsu all at once through a single API.

The project is built with **Kotlin and Spring Boot 4.0.3** on **Java 21**, using **Gradle 9.3.1** for builds.

**Repository**: https://github.com/Chesire/Gattai
**License**: Apache 2.0

---

## Learning Project Guidelines

This project is being used as a **learning vehicle** to understand backend architecture, Spring Boot API development, and related concepts. Interactions should reflect this educational focus.

### Code Changes - NO AUTOMATIC MODIFICATIONS
- **NEVER** automatically make code changes without explicit user consent
- Always discuss proposed changes, explain the approach, and wait for approval before implementing
- Provide explanations of WHY certain approaches are recommended

### Guidance Style - Hints Over Solutions
- Provide hints, ideas, and vague references to relevant classes/APIs instead of exact implementations
- Point toward appropriate Spring Boot annotations, patterns, or interfaces to explore
- Suggest architectural approaches rather than copy-paste code
- **Exception**: Only provide exact solutions when explicitly asked to do so
- This approach maximizes learning and understanding

---

## Common Commands

All commands use the Gradle wrapper (`./gradlew`), so no separate Gradle installation is needed.

### Build
```bash
./gradlew build           # Full build (compile, test, package)
./gradlew clean build     # Clean build from scratch
./gradlew assemble        # Compile and package without running tests
```

### Run
```bash
./gradlew bootRun         # Run the application locally
```

### Testing
```bash
./gradlew test                                # Run all tests
./gradlew test --tests GattaiApplicationTests # Run specific test class
./gradlew test -x                             # Build without running tests
```

### Code Quality & Output
```bash
./gradlew --info build    # Verbose build output
./gradlew --debug build   # Debug-level output
./gradlew tasks           # List all available Gradle tasks
```

### Clean & Reset
```bash
./gradlew clean           # Remove build artifacts
./gradlew --stop          # Stop Gradle daemon (useful if build hangs)
```

---

## Architecture Overview

### Current Structure

The project is a **single-module Spring Boot application** in its bootstrap phase with minimal implementation:

- **Main Entry Point**: `src/main/kotlin/com/chesire/gattai/GattaiApplication.kt`
  - Standard Spring Boot `@SpringBootApplication` entry point
  - Uses `runApplication<GattaiApplication>()` to start the server

- **Configuration**: `src/main/resources/application.properties`
  - Currently only sets `spring.application.name=gattai`

- **Tests**: `src/test/kotlin/com/chesire/gattai/GattaiApplicationTests.kt`
  - Basic `@SpringBootTest` that verifies Spring context loads

### Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Runtime |
| Kotlin | 2.2.21 | Language |
| Spring Boot | 4.0.3 | Framework |
| Gradle | 9.3.1 | Build tool |
| JUnit 5 | Latest | Testing framework |

### Compiler Configuration

- **Strict JSR-305**: `-Xjsr305=strict` enables strict null-safety checking
- **Annotation Targets**: `-Xannotation-default-target=param-property` sets default annotation targets
- These are configured in `build.gradle.kts` under the `kotlin` block

### Build Artifacts

After building, two JARs are created:
- `build/libs/gattai-0.0.1-SNAPSHOT.jar` - Executable Spring Boot JAR (includes embedded server)
- `build/libs/gattai-0.0.1-SNAPSHOT-plain.jar` - Plain library JAR

---

## Development Notes

### Project Stage

This project is in a **bootstrap/foundation phase**. The core infrastructure is set up, but no business logic has been implemented yet. As features are added:

- Create feature-specific packages under `com.chesire.gattai.*`
- Add controllers, services, and repositories following Spring Boot conventions
- Add comprehensive test coverage alongside implementation

### Add Dependencies

Edit `build.gradle.kts` in the `dependencies` block:

```kotlin
dependencies {
    implementation("org.example:library:1.0.0")
    testImplementation("org.example:test-library:1.0.0")
}
```

Then run `./gradlew build` to fetch and integrate.

### Key Files

- **build.gradle.kts** - Gradle build configuration
- **settings.gradle.kts** - Root project settings
- **gradlew / gradlew.bat** - Build scripts (don't edit, auto-generated)
- **gradle/** - Gradle wrapper files (don't edit, auto-generated)

---

## Troubleshooting

### Build Fails with Toolchain Error

If you see "Cannot find a Java installation," ensure:
1. Java 21 JDK (not JRE) is installed:
   ```bash
   java -version  # Should show "java 21"
   javac -version # Should show "javac 21" (proves it's full JDK)
   ```
2. `JAVA_HOME` is set correctly (see Environment Setup above)
3. Gradle daemon is stopped and restarted:
   ```bash
   ./gradlew --stop
   ./gradlew build
   ```

### Tests Pass But Build Fails

Run with debug output to diagnose:
```bash
./gradlew build --debug
```

---

## Additional Resources

- **Spring Boot Documentation**: https://spring.io/guides/gs/spring-boot/
- **Kotlin on Spring Boot**: https://spring.io/guides/tutorials/spring-boot-kotlin/
- **Gradle Documentation**: https://docs.gradle.org
