# CLAUDE.md

**⚠️ IMPORTANT**: If a `CLAUDE.local.md` file exists in this repository, it MUST be read first before proceeding with any work.

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Gattai** is a backend API service acting as a unified interface for querying and updating library tracking information across multiple platforms simultaneously (MyAnimeList, AniList, Kitsu).

Built with **Kotlin and Spring Boot 4.0.3** on **Java 21**, using **Gradle 9.3.1**.

**Repository**: https://github.com/Chesire/Gattai
**License**: Apache 2.0

---

## Learning Project Guidelines

This project is a **learning vehicle** for backend architecture and Spring Boot. Interactions should reflect this educational focus.

### Code Changes - NO AUTOMATIC MODIFICATIONS
- **NEVER** automatically make code changes without explicit user consent
- Always discuss proposed changes, explain the approach, and wait for approval before implementing

### Guidance Style - Hints Over Solutions
- Provide hints, ideas, and vague references to relevant classes/APIs instead of exact implementations
- Point toward appropriate Spring Boot annotations, patterns, or interfaces to explore
- **Exception**: Only provide exact solutions when explicitly asked

---

## Common Commands

All commands use the Gradle wrapper (`./gradlew`).

```bash
./gradlew build           # Full build (compile, test, package)
./gradlew clean build     # Clean build from scratch
./gradlew bootRun         # Run the application locally
./gradlew test            # Run all tests
./gradlew tasks           # List all available Gradle tasks
./gradlew --stop          # Stop Gradle daemon
```

**Environment variable required to run:**
```
MAL_CLIENT_ID=<your-mal-client-id>
```

---

## Architecture Overview

### Package Structure

```
com.chesire.gattai/
├── domain/                    # Core entities and service interfaces
│   ├── Ids.kt                 # Unified ID holder (kitsuId, malId, anilistId)
│   ├── Series.kt              # Output domain model + SeriesType enum
│   └── search/
│       └── SearchService.kt   # Interface implemented by all provider search services
├── feature/search/            # Search feature (controller + orchestration)
│   ├── SearchController.kt    # GET /api/v1/search endpoint
│   ├── SearchAggregator.kt    # Merges + deduplicates results across all providers
│   ├── SearchModel.kt         # Internal search result model
│   └── SearchParams.kt        # Request params (title, seriesType)
└── provider/                  # External API integrations
    ├── mapping/
    │   ├── SeriesIdMappingProvider.kt  # Loads anime-list-mini.json; resolves missing IDs
    │   └── SeriesIdMappingEntry.kt
    ├── anilist/               # GraphQL API (returns anilistId + malId)
    ├── kitsu/                 # REST JSONAPI (returns kitsuId; may include mal/anilist via mappings)
    └── mal/                   # REST API, requires MAL_CLIENT_ID (returns malId only)
```

### Key Architectural Patterns

- **Strategy**: `SearchService` interface with three provider implementations
- **Aggregation**: `SearchAggregator` fans out to all providers, then deduplicates by shared IDs
- **ID enrichment**: For anime with incomplete IDs, `SeriesIdMappingProvider` fills gaps using `anime-list-mini.json` (1.2MB pre-built cross-platform mapping database)
- Manga results skip the mapping provider lookup

### API Endpoint

`GET /api/v1/search?title=<title>&seriesType=ANIME|MANGA`

Returns `List<Series>` with unified IDs and deduplicated titles across all platforms.

### Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Runtime |
| Kotlin | 2.2.21 | Language |
| Spring Boot | 4.0.3 | Framework |
| Gradle | 9.3.1 | Build tool |
| JUnit 5 | via Spring Boot | Testing |
| MockK | 1.14.9 | Kotlin mocking in tests |
| Detekt | 1.23.8 | Static analysis / code quality |

### Configuration

- `src/main/resources/application.properties` — minimal config; `mal.client-id` is injected from env var `MAL_CLIENT_ID`
- `src/main/resources/mapping/anime-list-mini.json` — cross-platform anime ID mapping database, loaded at startup

---

## Development Notes

### Adding Dependencies

Edit the `dependencies` block in `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.example:library:1.0.0")
    testImplementation("org.example:test-library:1.0.0")
}
```

### Key Files

- **build.gradle.kts** — Gradle build config (dependencies, Detekt, Kotlin compiler options)
- **settings.gradle.kts** — Root project settings
- **src/main/resources/mapping/anime-list-mini.json** — Do not regenerate casually; large static resource

### Compiler Configuration

- `-Xjsr305=strict` — strict null-safety checking
- `-Xannotation-default-target=param-property` — default annotation targets
- JVM target: 21