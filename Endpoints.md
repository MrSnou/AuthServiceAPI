Postman Endpoints


| Method | Endpoint             | Description                           |
|--------|----------------------|---------------------------------------|
| `POST` | `/api/auth/register` | New user register                     |
| `POST` | `/api/auth/login`    | Login (returns JWT cookie)            |
| `POST` | `/api/auth/refresh`  | Token refresh                         |
| `POST` | `/api/auth/logout`   | Logout (deletes JWT cookie)           |
| `POST` | `/api/auth/me`       | User details                          |
| `GET`  | `/api/auth/public`   | Public endpoint without authorization |
| `GET`  | `/api/auth/private`  | Public endpoint with authorization    |
| `GET`  | `/api/secure/**`     | Endpoints secured with authrization   |
