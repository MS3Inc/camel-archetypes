#set($h3 = '###')
#set($h4 = '####')
# README

$h3 ${artifactId} 
* Version: ${version}
* Most up to date implementation will be found in branch develop. 

$h3 Getting Started

**Running Locally using IDE**

Use the following environment variables: 
   * ```spring.profiles.active=<env>```
   * ```spring.config.name=${artifactId}```

**Running on Command Line**

```
mvn spring-boot:run -Dspring-boot.run.profiles=<env> -Dspring-boot.run.arguments="--spring.config.name=${artifactId}"
```

$h3 How to Test

[Placeholder]

$h3 Actuator Endpoints

To access the list of available Actuator endpoints, go to: http://localhost:8080/actuator or `{{url}}/actuator`

The available endpoints are as follows:

* `/health`
* `/metrics`
* `/info`

#### Health Indicator

Sample response below when you use: http://localhost:8080/actuator/health

```
{
    "status": "UP",
    "components": {
        "camelHealth": {
            "status": "UP",
            "details": {
                "name": "camel-health-check",
                "context": "UP",
                "route:get-answers": "UP",
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 1000068870144,
                "free": 569233043456,
                "threshold": 10485760,
                "exists": true
            }
        },
        "ping": {
            "status": "UP"
        }
    }
}
```

$h4 Metrics

List of available metrics can be found here: http://localhost:8080/actuator/metrics/

Add the metric name in `/metrics/<metric name>` to access the metric for that particular topic.

Sample metric: http://localhost:8080/actuator/metrics/jvm.memory.used

```
{
    "name": "jvm.memory.used",
    "description": "The amount of used memory",
    "baseUnit": "bytes",
    "measurements": [
        {
            "statistic": "VALUE",
            "value": 2.71031904E8
        }
    ],
    "availableTags": [
        {
            "tag": "area",
            "values": [
                "heap",
                "nonheap"
            ]
        },
        {
            "tag": "id",
            "values": [
                "G1 Old Gen",
                "CodeHeap 'non-profiled nmethods'",
                "G1 Survivor Space",
                "Compressed Class Space",
                "Metaspace",
                "G1 Eden Space",
                "CodeHeap 'non-nmethods'"
            ]
        }
    ]
}
```

$h4 Info

Sample response below when you use: http://localhost:8080/actuator/info

```
{
    "camel.name": "${artifactId}",
    "camel.version": "3.5.0",
    "camel.uptime": "1 minute",
    "camel.uptimeMillis": 91627,
    "camel.status": "Started"
}
```


$h3 Contact
* Full name (MS3 email)