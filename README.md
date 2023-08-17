# Дипломный проект профессии «Тестировщик»

Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Документы
* [Задание](https://github.com/FecklaSveckla/Diplom/blob/main/docs/Zadanie.md)
* [План автоматизации](https://github.com/FecklaSveckla/Diplom/blob/main/docs/Plan.md)
* [Отчет по итогам тестирования](https://github.com/FecklaSveckla/Diplom/blob/main/docs/Report.md)
* [Отчет по итогам автоматизированного тестирования](https://github.com/FecklaSveckla/Diplom/blob/main/docs/Summary.md)

Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

На локальном компьютере заранее должны быть установлены IntelliJ IDEA, Docker Desktop? Dbeaver

## Процедура запуска авто-тестов:

**1.** Склонировать на локальный репозиторий [Дипломный проект](https://github.com/FecklaSveckla/Diplom) 

**2.** Запустить Docker Desktop

**3.** Открыть проект в IntelliJ IDEA

**4.** В терминале запустить контейнеры:

    docker-compose up -d

**5.** Запустить целевое приложение:

     для mySQL: 
    java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar

     для postgresgl:
     java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

**6.** Проверить доступность приложения в браузере по адресу: http://localhost:8080/

**7.** Открыть второй терминал

**8.** Во втором терминале запустить тесты:

    для mySQL:
    ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

    для postgresgl: 
    ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"

**9.** Создать отчёт Allure и открыть в браузере:

    ./gradlew allureServe

**10.** Для завершения работы allureServe выполнить команду:

    Ctrl+C

и подтверждаем действие в терминале вводом Y.
    
**11.** Для остановки работы контейнеров выполнить команду:

    docker-compose down

