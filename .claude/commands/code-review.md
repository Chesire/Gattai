You are performing a comprehensive code review of this Spring Boot / Kotlin project.

## Step 1: Ask clarifying questions

Use AskUserQuestion to ask the following before doing any work:

1. **Review scope**: Should the review cover the entire codebase, or only the changes on the current branch?
   - Options: "Entire codebase" | "Current branch changes only"

2. **Output format**: Where should findings be written?
   - Options: "Write to code-review.md in the project root" | "Output inline in the chat"

3. **Extra review areas** (multi-select): Which additional areas beyond the core three (architecture, performance/bugs, security)?
   - Options: "Test coverage", "Code style / Detekt", "None — keep to the core three"

---

## Step 2: Determine scope

If the user chose **"Current branch changes only"**, run:
```bash
git diff master...HEAD --name-only
```
Pass the list of changed files to each agent so they focus only on those files.

If the user chose **"Entire codebase"**, agents should read all source files under `src/`.

---

## Step 3: Launch 3 parallel Explore agents

Launch all three agents **in a single message** (parallel, background). Substitute `[scope]` with either "the entire codebase" or "the following changed files: [list]".

---

### Agent 1 — Architecture & Code Style

> Review [scope] of this Spring Boot / Kotlin project at /mnt/dev/Gattai for architecture and code style issues.
>
> **Spring Boot architecture checks:**
> - Are Spring stereotypes (`@Service`, `@RestController`, `@Component`, `@Repository`) used correctly and consistently?
> - Is dependency injection done via constructor injection? Field-level `@Autowired` is discouraged.
> - Is layering correct — domain, feature, provider? Do any layers import types from layers they should not depend on?
> - Are beans scoped correctly? Stateless beans should be singletons (default). Stateful beans need explicit scoping.
> - Does any class have too many responsibilities (SRP)?
> - Is error handling at the controller appropriate? Is there a `@RestControllerAdvice` / `@ControllerAdvice` for centralised exception handling? Are correct HTTP status codes returned?
> - Are configuration values externalised to `application.properties` / `@ConfigurationProperties`? Are there hardcoded URLs, credentials, or magic values?
> - Are `@Transactional` methods: (a) public only — Spring AOP proxies cannot intercept private/internal calls; (b) placed on the service layer, NOT on controllers; (c) using `readOnly = true` for non-modifying queries?
> - Do REST controllers return DTOs, NOT raw JPA entities?
> - Are services stateless — no mutable shared fields?
> - Are Spring profiles (`application-dev.properties`, `application-prod.properties`) used to separate environment config?
> - Are `@Async` methods (if any): returning `void` or `Future`/`CompletableFuture`; using a custom thread pool executor (not the default `SimpleAsyncTaskExecutor` which can cause memory leaks); never called from within the same class (proxy limitation)?
> - Are Actuator endpoints configured? If so, are they protected? `/actuator/env` and `/actuator/heapdump` can leak credentials and must never be publicly exposed.
> - Are there circular or unexpected dependencies?
>
> **Kotlin idiom checks:**
> - Are Kotlin idioms used appropriately — `data class`, sealed classes, scope functions (`let`, `run`, `apply`), extension functions?
> - Prefer `val` over `var` unless mutation is explicitly needed.
> - Are there unsafe `!!` operators or risky null handling?
> - Are there magic strings/numbers that should be constants or enums?
> - Are there dead code, unused imports, or redundant operations?
> - Are `when` expressions exhaustive (sealed classes, enums)?
> - Is `java.time` used instead of legacy `Date`/`Calendar`?
> - Is SLF4J (`log.info`, `log.error`) used instead of `System.out.println`?
> - Are there any Detekt suppressions without a justifying comment?
>
> For each finding output: **[SEVERITY]** Title — file:line — explanation and suggested fix.
> Severity: CRITICAL / MAJOR / MINOR. Only report what you actually read — do not hallucinate.

---

### Agent 2 — Performance & Bugs

> Review [scope] of this Spring Boot / Kotlin project at /mnt/dev/Gattai for performance issues and bugs.
>
> **Performance checks:**
> - Are there unbounded caches or collections that grow without eviction — potential memory leaks?
> - Is expensive startup work (e.g. loading large JSON files, building indexes) done once at startup or on every request?
> - Are HTTP clients configured with connection and read timeouts? Unconfigured clients block threads indefinitely on slow/unresponsive upstreams.
> - Are there N+1-style request patterns — a call made inside a loop that could be batched?
> - Are inefficient data structures used (e.g. `List` for repeated `.contains()` lookups where a `Set`/`Map` would be O(1))?
> - Are read-only service methods annotated `@Transactional(readOnly = true)`? This enables database-level optimisations.
> - Is `findAll()` called without pagination on potentially large datasets?
> - Are there redundant computations inside request paths — things computed per request that could be computed once?
> - Are collections iterated multiple times when a single pass would suffice?
> - Is connection pooling configured appropriately for the expected load?
>
> **Bug checks:**
> - Are there unchecked nulls, dangerous `!!` operators, or unsafe casts?
> - Is deduplication / aggregation logic correct for all edge cases — null IDs, partial matches, duplicate entries?
> - Are exceptions caught too broadly (catching `Exception` or `Throwable`), hiding real failures?
> - Are there race conditions under concurrent access — shared mutable state accessed from multiple threads?
> - Do all `when` expressions cover all cases? Are there implicit else branches that could swallow unexpected values?
> - Is user input validated before use in external calls?
> - Are HTTP error responses handled distinctly from "no results"? Errors silently converted to empty collections hide provider failures.
> - Is `@Transactional` applied to `private` methods or called from within the same class? Both silently skip the transaction.
> - Are checked exceptions handled? By default Spring only rolls back on unchecked (runtime) exceptions — checked exceptions commit the transaction unless `rollbackFor` is specified.
> - Is JSON deserialisation error-safe? What happens when an external API changes its response shape?
>
> For each finding output: **[SEVERITY]** Title — file:line — explanation and suggested fix.
> Severity: CRITICAL / MAJOR / MINOR. Only report what you actually read — do not hallucinate.

---

### Agent 3 — Security

> Review [scope] of this Spring Boot / Kotlin project at /mnt/dev/Gattai for security vulnerabilities (OWASP Top 10 and Spring-specific).
>
> **Input & injection checks:**
> - Is user input (query params, request bodies) validated and sanitised before use in queries, URLs, or external API calls?
> - Is there injection risk — GraphQL injection, URL parameter injection (unencoded user input appended to query strings), header injection?
> - Are parameterised queries / `UriComponentsBuilder` used rather than string concatenation for URLs?
> - Is input length limited? Unbounded strings can exhaust external API limits.
>
> **Secrets & data exposure:**
> - Are secrets (API keys, tokens, passwords) ever hardcoded, returned in responses, or written to logs?
> - Are environment variables validated at startup — silent failure if a required env var is missing?
> - Do error responses leak stack traces, class names, file paths, or Spring internals?
>
> **Spring-specific security:**
> - Is Spring Security configured? If not, are all endpoints intentionally public?
> - Are Actuator endpoints exposed? `/actuator/env` exposes all environment variables (including secrets) in plaintext — must be secured or disabled.
> - Is CORS configured explicitly? Default Spring Boot behaviour may be overly permissive in production.
> - Is CSRF configured? For stateless REST APIs it is typically disabled intentionally — but this should be explicit.
> - Is there any rate limiting on public endpoints?
> - Are there any method-level security annotations (`@PreAuthorize`) where needed?
>
> **Dependencies:**
> - Check `build.gradle.kts`: are any dependency versions known to have CVEs? Flag any outdated major versions.
> - Is there a dependency vulnerability scanning tool (OWASP Dependency-Check, Snyk) configured in CI?
>
> **Logging:**
> - Is sensitive data (user search queries, API keys, tokens) written to logs?
> - Are correlation IDs used for request tracing?
>
> For each finding output: **[SEVERITY]** Title — file:line — explanation and suggested fix.
> Severity: CRITICAL / MAJOR / MINOR. Only report what you actually read — do not hallucinate.

---

### Agent 4 — Test Coverage (only if user selected this extra area)

> Review [scope] of this Spring Boot / Kotlin project at /mnt/dev/Gattai for test coverage gaps and test quality.
>
> **Coverage checks:**
> - What percentage of the business logic has unit tests? Flag untested public methods.
> - Are there integration tests for the REST endpoints (e.g. `@SpringBootTest` + `MockMvc` or `WebTestClient`)?
> - Are external HTTP dependencies mocked in tests (e.g. WireMock, `@MockBean`)?
> - Are edge cases tested — null IDs, empty results, provider errors, malformed input?
> - Are there tests for the deduplication and ID-enrichment logic covering all ID-null combinations?
>
> **Test quality checks:**
> - Do tests assert meaningful behaviour, not just that no exception is thrown?
> - Are `@DataJpaTest` / `@WebMvcTest` slice tests used where appropriate instead of full `@SpringBootTest`?
> - Are test secrets hardcoded in test source files checked into version control?
> - Are tests isolated — no shared mutable state between tests?
> - Is `@Transactional` used on tests so DB changes are rolled back automatically?
>
> For each finding output: **[SEVERITY]** Title — file:line — explanation and suggested fix.
> Severity: CRITICAL / MAJOR / MINOR. Only report what you actually read — do not hallucinate.

---

## Step 4: Synthesise findings

Once all agents complete:

1. De-duplicate findings reported by multiple agents — merge into one entry, noting it was flagged by multiple agents.
2. Group findings by severity: CRITICAL first, then MAJOR, then MINOR.
3. Within each severity group, organise by category: Architecture → Bugs → Security → Performance → Code Style → Test Coverage.
4. For each finding include:
   - Severity badge: `**[CRITICAL]**` / `**[MAJOR]**` / `**[MINOR]**`
   - Short descriptive title
   - File path and line number
   - Clear explanation of the problem (including *why* it matters)
   - Concrete suggested fix or direction

5. End with a summary table:

| Severity | Count | Top themes |
|----------|-------|------------|
| CRITICAL | N | ... |
| MAJOR | N | ... |
| MINOR | N | ... |

---

## Step 5: Output

Based on the user's answer to the output format question:

- **Write to file**: Write the complete findings to `code-review.md` in the project root. Tell the user the file has been written and how many findings were found at each severity level.
- **Inline**: Output the findings directly in the chat response.