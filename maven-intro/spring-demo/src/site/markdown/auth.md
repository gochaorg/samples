Настройка аунтификации
========================

Используется базовая аунтификация ([basic](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication))

Аунтификация настраивается в файле `application.yml`

```yml
# Настройки авторизации
basic-auth:
  # Аккануты
  accounts:
    - username: usr1  # Логин
      password: pswd1 # Пароль
    - username: usr2
      password: pswd2
```