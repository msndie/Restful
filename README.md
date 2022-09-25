# Restful
Goal of this project is to learn how to use Spring mechanisms to develop REST applications.

<details>
<summary>Schema</summary>
<img src="/screenshots/schema.png">
</details>
<details>
<summary>Api</summary>
<img src="/screenshots/api.png">
</details>
<details>
<summary>Adoc</summary>
<img src="/screenshots/publish_adoc_documentation.png">
</details>

In this project we use:
- Rest Controllers
- Spring security (OAuth2 server) in ex01 and ex02 for JWT role based auth. GET operations are available for user with any role, but POST, PUT and DELETE only for ADMIN
- Hibernate
- Spring Data Jpa
- Spring Data Rest in ex02
- Swagger
- Mockito and Jupiter for tests

# Launch
You need to create an empty database in postgres.

Write your credentials in ```application.properties``` file in each EX.

Then you can just use the following command to run the application
```
./mvnw clean spring-boot:run
```

Now service available on <b>http://localhost:8080</b>. To access it in ex01 and ex02 you need to get JWT token first.
To do so you need to send POST request with following information on http://localhost:8080/signUp
```
{
  "login": "admin",
  "password": "123QWEasd"
}
```
This way you get your token, you need to add it in Authorization header with each request.
You can find available mappings on <b>http://localhost:8080/swagger-ui/</b> page for ex00 and ex01.
For ex02 you can use HAL browser
