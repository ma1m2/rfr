# rfr-auth

Spring Authorization Server: регистрация, логин, OAuth 2.1 / OIDC, схема `rangiffler-auth`. Порт **9001**. Подробности — `rfr-auth/README.md`.

## Запуск

MySQL уже должен слушать `localhost:3306` (`root` / `secret`); если нет — `localenv.sh`. Из корня репозитория: `./gradlew :rfr-auth:bootRun`. Проверка: `GET http://localhost:9001/.well-known/openid-configuration`.

## Архитектура

- Пакеты: `config`, `controller`, `data`, `model`, `service`, `validation`. Новые классы — в существующие пакеты.
- Страницы login / register / error — Thymeleaf (`src/main/resources/templates`), не REST JSON.
- Клиент фронтенда — публичный (`client-id: client`, без секрета), Authorization Code + PKCE. Redirect: `http://localhost:3001/authorized`.
- Регистрация пишет только в `rangiffler-auth`. API-профиль создаёт `rfr-api` при первом `Query.user`.
- RSA JWKS генерируется при старте: после рестарта ранее выданные JWT недействительны.
- `/oauth2/revoke` для SPA — только через `PublicClientRevocationAuthenticationConverter` / `Provider`. Не ломай этот обход: стандартный SAS отвечает `401 invalid_client`.
- Access token предназначен `rfr-api` (`aud: rangiffler-api`). `id_token` — только фронту (logout `id_token_hint`).

## Данные

Миграции: `src/main/resources/db/migration/rangiffler-auth`. Таблицы: `user`, `authority`. OAuth-сессии и ключи в БД не хранятся.
