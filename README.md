# Task Management System

Система управления задачами 

---

## Требования

- **Java 17** (для локального запуска)
- **Docker и Docker Compose** (для запуска через Docker)
- **Gradle** (для сборки проекта)

---

## Локальный запуск

1. **Клонировать репозиторий**  
   Склонируйте репозиторий с проектом:  
   [https://github.com/PavelOkhrimchuk/TaskManagementSystem.git](https://github.com/PavelOkhrimchuk/TaskManagementSystem.git)

2. **Настройка базы данных**  
   Убедитесь, что PostgreSQL работает локально, и настройте `application-local.properties` следующим образом:

   ```properties
   spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
   spring.datasource.username=<username>
   spring.datasource.password=<password>
   spring.datasource.driver-class-name=org.postgresql.Driver

   ```
 3.   **Запуск приложения**  
    Убедитесь, что в application.properties установлено:

 ```properties
    spring.profiles.active=local
   ```
    
 4.  **Swagger UI для взаимодействия с API будет доступен по адресу** 
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) ![image](https://github.com/user-attachments/assets/31d8807c-a6be-447e-b23f-dce3bc856875)

### Основные функции API:
- **Authentication**:  
  - Регистрация/логин юзера 

- **Admin**:  
  - Изменение роли пользователя.  

- **Tasks**:  
  - Управление задачами  

- **Comments**:  
  - Добавление комментариев к задачам.   

## Запуск через Docker Compose

1. **Убедиться в наличии Docker**  
   Убедитесь, что Docker и Docker Compose установлены. Если Docker отсутствует, скачайте его с [официального сайта Docker](https://www.docker.com/).

2. **Запуск приложения**  
   Выполните команду для сборки и запуска всех сервисов через Docker Compose:  
   `docker-compose up --build`  

   После запуска Swagger UI будет доступен по адресу:  
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
