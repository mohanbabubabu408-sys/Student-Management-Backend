# Getting Started

## Database configuration

The application connects to MySQL. Set `DB_PASSWORD` to the password for the
configured MySQL user before starting it. The user defaults to `root`; set
`DB_USERNAME` to override it. `DB_URL` can be set to use a different MySQL
host, port, or database.

In PowerShell, for example:

```powershell
$env:DB_PASSWORD = "your-mysql-password"
.\mvnw.cmd spring-boot:run
```

The application test uses an in-memory H2 database and does not require MySQL.

## Department API

Departments are managed at `/api/departments`. Create one with:

```json
{
  "departmentCode": "IT",
  "departmentName": "Information Technology"
}
```

The existing Student request and response shape is preserved: its `department`
value is still a string. When creating or updating a student, set that value to
an existing department code or to an unambiguous department name. Student
responses continue to show the department name.

Hibernate creates or updates the `departments` table and adds the
`students.department_id` relationship in the existing database. Existing
non-empty student department values are linked to matching departments at
startup; the original `students.department` text column is retained to avoid
discarding existing data.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.2-SNAPSHOT/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.2-SNAPSHOT/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.2-SNAPSHOT/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.2-SNAPSHOT/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
