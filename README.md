# library
Использованный стек технологий
- Spring Boot
- Hibernate (Spring Data JPA)
- H2 in-memory DB
- jUnit и Mockito

Весь код покрыт тестами:
- Тесты сервисов для работы с данными
- Тесты контроллеров
- Тесты REST со стороны пользователя

Для тестирования используется отдельная база данных в памяти (отдельный конфиг).
Все тестовые методы можно запускать отдельно.

Дополнительные поля отличные от задания не используются.
Для реализации исключения дубликатов и хранения у книги нескольких авторов используется связь ManyToMany, у Author при сериализации игнорируются его книги для избежания рекурсии

Примененный шаблон проектирования - иньекция зависимостей.
