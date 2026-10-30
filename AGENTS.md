# Rangiffler (rfr)

Учебный проект курса Java Advanced 3.0. Монолит с моками: часть функций настоящая (регистрация, вход, профиль, дружба), часть возвращается из JSON-моков (фото, лайки, лента, статистика).

## Модули и порты

| Модуль | Что это | Порт | AGENTS.md |
|---|---|---|---|
| `rfr-auth` | Spring Authorization Server, схема `rangiffler-auth` | 9001 | [AGENTS.md](rfr-auth/AGENTS.md) |
| `rfr-api` | GraphQL API `/graphql`, схема `rangiffler-api`; фото и лента — моки из `src/main/resources/mock/*.json` | 8081 | [AGENTS.md](rfr-api/AGENTS.md) |
| `rfr-gql-client` | React + Apollo, собирается отдельно от Gradle (`npm run dev`) | 3001 | [AGENTS.md](rfr-gql-client/AGENTS.md) |
| `rfr-e2e` | E2E-тесты: JUnit 6, Playwright Java, Allure 3 | — | [AGENTS.md](rfr-e2e/AGENTS.md) |

MySQL — порт 3306, пользователь `root`, пароль `secret`.

## Окружение

- MySQL: `localenv.sh` (`docker compose up -d mysql --wait`).
- Оба бэкенда: `./gradlew runAll` (`rfr-auth` 9001 и `rfr-api` 8081).
- Фронт: в `rfr-gql-client` — `npm ci` (один раз), затем только команду `npm run dev` (`http://localhost:3001`).
- Нужны JDK 25, Node 22+, Docker.

## E2E

Стенд уже должен быть поднят. Один раз на машину: `./gradlew :rfr-e2e:installPlaywright` (Chromium).

<<<<<<< HEAD
- Все тесты: `./gradlew e2e`
- Один класс: `./gradlew :rfr-e2e:test --tests '*RegistrationWebTest'`
- Один метод: `./gradlew :rfr-e2e:test --tests '*RegistrationWebTest.shouldAuthorizeAfterRegistration'`
=======
Настройки тестов — `TestConfig`: `-Drfr.<ключ>=…` или переменная `RFR_<КЛЮЧ>`, иначе значение по умолчанию. Подробности — `rfr-e2e/README.md`.

## Правила для rfr-e2e

- Структура пакетов — `rfr-e2e/README.md`, раздел «Архитектура»: `config`, `jupiter`, `data`, `api`, `ui`, `assertions`, `test`. Новые классы кладутся в существующие пакеты.
- Page Objects и компоненты — по skill `rfr-page-objects` (`.cursor/skills/rfr-page-objects/SKILL.md`).
- Без комментариев и Javadoc в коде `rfr-e2e` (Java, Gradle, SQL, properties): смысл передают имена и `@Step`.
- Вход в тестах только через UI; токены в localStorage не подставлять.
- Проход сценария через Playwright MCP — не запуск теста. Успех подтверждается JUnit-прогоном через Gradle.
- Тестовые данные создаются и удаляются фикстурами (`@User` → `UserExtension` → `UserFixture`), без широких `DELETE`/`TRUNCATE`.
>>>>>>> origin/master
