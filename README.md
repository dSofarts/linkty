# Проект сервиса коротких ссылок Linkty

В проекте вы можете создавать короткие ссылки, задавать лимит переходов и время их действий, 
а также отслеживать статистику по переходам. Проект представляет собой backend модуль, написанный 
на Java и Spring Boot, а также frontend модуль, написанный на Angular

## Запуск

Для запуска данного проекта, необходим Docker и Maven. Для начала необходимо собрать проект 
при помощи Maven:

    mvn clean package

После этого необходимо запустить build через docker compose

    docker compose up -d

По завершении выполнения команды у вас будет развернуты:
1) linkty-api - backend сервис. Работает на порту 8080
2) linkty-front - frontend сервис. Работает на порту 4200
3) database - PostgreSQL. Работает на порту 5432

## Работа с проектом

Основная **_точка входа_** находится на http://localhost:4200, где пользователь может создавать 
короткие ссылки, отслеживать статистику и другое.

Для работы со **swagger** необходимо перейти по http://localhost:8080/swagger-ui

## Прочие данные

По умолчанию строку действия короткой ссылки установлен как 7 дней. При этом, фактический срок 
действий берется как меньший из заданного пользователем и установленным по умолчанию. 
Для смены максимального срока действия в системе, цифра в днях указываемся в файле docker-compose:

    docker-compose.yml

Срок указывается в конфигурации api:

    LINK_EXPIRED: 7