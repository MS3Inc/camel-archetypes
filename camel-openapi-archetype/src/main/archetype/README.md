#set($h3 = '###')
#set($h4 = '####')
# ${artifactId}

This API project was generated using the [Camel OpenAPI Archetype](https://github.com/MS3Inc/camel-archetypes).

$h3 Getting Started

**Running on Command Line**

```
mvn spring-boot:run
```

<!-- 
**Running Locally using IDE**

This project uses Spring profiles, and corresponding ${artifactId}-<env>.yaml files.

Use the following environment variables: 
   * ```spring.profiles.active=<env>```
   * ```spring.config.name=${artifactId}```

**Running on Command Line**

```
mvn spring-boot:run -Dspring-boot.run.profiles=<env> -Dspring-boot.run.arguments="--spring.config.name=${artifactId}"
```
-->

$h3 Actuator Endpoints

To access the list of available Actuator endpoints, go to: http://localhost:8080/actuator or `{{url}}/actuator`

The available endpoints are as follows:

* `/health`
* `/metrics`
* `/info`

$h4 Metrics

List of available metrics can be found here: http://localhost:8080/actuator/metrics/

Add the metric name in `/metrics/<metric name>` to access the metric for that particular topic.

Sample metric: http://localhost:8080/actuator/metrics/jvm.memory.used

```
{
    "name": "jvm.memory.used",
    "description": "The amount of used memory",
    ...
}
```

$h3 Contact
* Full name (email)