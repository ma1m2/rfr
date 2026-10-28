# rfr-gql-client

React 19 + Apollo SPA. Dev-сервер: **3001**. Собирается отдельно от Gradle (`npm run dev`). Подробности — `rfr-gql-client/README.md`.

## Запуск

Нужны `rfr-auth` (9001) и `rfr-api` (8081). В каталоге модуля: `npm ci`, затем `npm run dev`. URL и клиент — `.env` (`VITE_AUTH_URL`, `VITE_API_URL`, `VITE_FRONT_URL`, `VITE_CLIENT_ID=client`). Порт 3001 должен совпадать с redirect URI в `rfr-auth`. Проверки: `npm run lint`, `npm run build`.

## Архитектура

- GraphQL — хуки в `src/hooks` (одноимённые `operationName`). HTTP/OAuth — `src/api`. Страницы — `src/pages`. UI — `src/components`. Не плодить новые корни, пока фича не выпадает из существующих.
- Компоненты брать из MUI, не собирать заново то, что библиотека уже даёт.
- В `rfr-api` уходит только `access_token` (Bearer). `id_token` — лишь `id_token_hint` при logout.
- Защищённые маршруты — `PrivateRoute` + `GetUser` (этот запрос создаёт API-профиль при первом входе).
- Лента, фото и лайки на бэкенде mock: успешный snackbar или обновлённый кэш Apollo не доказывают сохранение. После перечитывания `GetFeed` данные вернутся к JSON.

## Маршруты

`/` — лендинг, `/redirect` — PKCE + authorize, `/authorized` — обмен code, `/logout` — очистка localStorage, `/my-travels`, `/people`, `/profile` — приложение.
