# Дипломный проект "QA автоматизатор на Java"

## Часть 2. API-тесты

### Использованные библиотеки

- Java11
- Maven 3.8.1
- JUnit 4.13.2
- RestAssured 5.3.0
- Gson 2.10.1
- maven-surefire-plugin 2.22.2
- Allure libs 2.21.0

### Покрытие

**Создание пользователя:**

- создать уникального пользователя;
- создать пользователя, который уже зарегистрирован;
- создать пользователя и не заполнить одно из обязательных полей.

**Логин пользователя:**

- логин под существующим пользователем,
- логин с неверным логином и паролем.

**Изменение данных пользователя:**

- с авторизацией,
- без авторизации,

**Создание заказа:**

- с авторизацией,
- без авторизации,
- с ингредиентами,
- без ингредиентов,
- с неверным хешем ингредиентов.

**Получение заказов конкретного пользователя:**

- авторизованный пользователь,
- неавторизованный пользователь.

### Запуск тестов

Запуск теста: ```mvn clean test```

Запуск отчета Allure: ``` mvn allure:serve```

Отчет о покрытии кода: ```target/allure-results```

