# Gattai
Backend api project to update multiple library tracking sources at once.

## Prerequisites

The following environment variables are required to run the application:

| Variable | Description |
|----------|-------------|
| `MAL_CLIENT_ID` | Client ID for the MyAnimeList API. Obtain one by registering an application at [myanimelist.net/apiconfig](https://myanimelist.net/apiconfig). |

## Profiles

The application supports the following Spring profiles:

| Profile | Description |
|---------|-------------|
| _(none)_ | Default production profile. Swagger UI is disabled. |
| `dev` | Enables Swagger UI at `/swagger-ui.html` for local development and debugging. |

To run with the `dev` profile:

```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```
