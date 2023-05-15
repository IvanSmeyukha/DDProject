**Система управлениями проектами.**

Основная цель: разработать систему управления проектами.

![ddproject drawio (2)](https://github.com/IvanSmeyukha/DDProject/assets/87076117/6eee9716-5e86-4861-880d-b0bffbe0eda5)


Описание модулей:
 1)	Модуль web отвечает за:
    -	прием запросов пользователей;
    -	перенаправление запросов в соответствующий сервис модуля services;
    -	получение ответа из сервиса и отправка его пользователю.
 2)	Модуль business отвечает за бизнес-логику приложения и содержит модули services, repository, model.
 3)	Модуль services содержит сервисы, реализующие логику обработки запросов.
 4)	Модуль repository отвечает за работу с хранилищем данных.
 5)	Модуль model содержит представления сущностей из хранилища данных.
 6)	Модуль dto содержит трансферные объекты для передачи информации между модулями.

Задачи:
 1)	Спроектировать архитектуру приложения.
 2)	Спроектировать базу данных.
 3)	Реализовать следующие функции:
    -	Управление сотрудниками компании. 
    -	Управление проектами и их жизненным циклом.
    -	Управление задачами внутри проекта и их жизненным циклом.
    -	Управление командами внутри проекта.
    -	Аутентификация в системе.
 4)	Покрыть разработанное приложение тестами.

