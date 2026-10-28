# rfr-api

GraphQL Resource Server (`POST /graphql`), схема `rangiffler-api`. Порт **8081**. JWT проверяется по JWKS `rfr-auth`. Подробности — `rfr-api/README.md`.

## Запуск

Нужны MySQL (`localhost:3306`, `root` / `secret`; если нет — `localenv.sh`) и запущенный `rfr-auth` на 9001. Из корня: `./gradlew :rfr-api:bootRun`. GraphQL-модель из схемы генерирует DGS (`generateJava`); не править классы в `build/generated`.

## Что настоящее, что mock

| Настоящее (БД) | Mock (JSON, ничего не пишут) |
|---|---|
| `Query.user` / `users` / `countries`, `Mutation.user` / `friendship` | `Query.feed`, `Mutation.photo` / `deletePhoto` |

Таблицы `photo`, `like`, `photo_like`, `statistic` в миграции есть, приложение их не использует. Моки должны оставаться в проекте до явного указания на необходимость их замены на реальные реализации.   

## Архитектура

- Пакеты: `config`, `controller`, `data`, `exception`, `service`, `util`. Источник схемы — `src/main/resources/graphql/query.graphqls`.
- Query-контроллеры: `@PreAuthorize("hasAuthority('read')")`. Mutation: `hasAuthority('write')`. В GraphQL уходит access token, не `id_token`.
- Username текущего пользователя — JWT `sub`. UUID в `rangiffler-api` и `rangiffler-auth` независимы, связь только по `username`.
- Первый `Query.user` создаёт профиль со страной `ru`, если его ещё нет.
- Ошибки домена — через `GraphQlExceptionResolver` (`NOT_FOUND` / `BAD_REQUEST`). HTTP GraphQL остаётся 200.
- Моки ленты: `src/main/resources/mock/query_feed.json` и `query_feed_with_friends.json`.

## Данные

Миграции: `src/main/resources/db/migration/rangiffler-api`. `ddl-auto: none`.
