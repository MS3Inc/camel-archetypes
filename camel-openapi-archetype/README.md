# README #

This is a Maven archetype that generates a Camel/SpringBoot application with stubs for API endpoints generated from a provided OpenAPI document.
It is part of MS3's integration platform, [Tavros](https://github.com/MS3Inc/tavros).

### How do I get set up? ###

Temporary instructions:
This version of the archetypes is not released and needs to be installed locally.
Clone both the camel-restdsl-openapi-plugin (branch: arch-version-updates) and camel-archetypes (branch: version-updates) repos and install them locally.

Linux/bash instructions:
```
mkdir arch-0-28-SNAPSHOT
cd arch-0-28-SNAPSHOT
git clone https://github.com/MS3Inc/camel-restdsl-openapi-plugin.git
git clone https://github.com/MS3Inc/camel-archetypes.git
cd camel-restdsl-openapi-plugin
git checkout 21c33cc834ae91c362bdb722d14f8c1a6918f075
mvn clean install
cd ../camel-archetypes
git checkout 56ded315199b081905c141e26674a5678f889c9c
mvn clean install
```

To generate an API using the archetype you just installed locally, cd to your API directory and run the command below with version `-DarchetypeVersion=0.2.8-SNAPSHOT`.

### How do I run the archetype? ###

To run the archetype, `cd` to the directory where the new project will live and then run:

```bash
mvn archetype:generate \
-DarchetypeGroupId=com.ms3-inc.tavros \
-DarchetypeArtifactId=camel-openapi-archetype \
-DarchetypeVersion=<check-for-latest> \
-DgroupId=<groupId> \
-Dversion=0.1.0-SNAPSHOT \
-DartifactId=<project-name> \
-DspecificationUri=<path-of-your-spec>
````

Anything surrounded by an angle bracket in the above command should be replaced with the value you want. All the properties listed in the above command are required, and if any are removed/not provided, they will be prompted for. The exception to this is the `specificationUri` (OpenAPI file), where the default sample specification will be used if one isn't provided. To override this in interactive mode, when prompted for `Y`, enter something else such as `n`, and then re-provide the provided information and provide the specification when prompted.

The `-DpackageInPathFormat` and `-package` arguments should be supplied if your groupId has a dash in it, such as com.ms3-inc.  Leave them out if not needed. Here is an example of providing them:
```bash
-DpackageInPathFormat \
-Dpackage=com.ms3_inc.tavros \
-DgroupId=com.ms3-inc.tavros \
```

### To use the Dockerfile locally

```
mvn clean package
docker build -t <tag> .
docker run -p 9000:9000 -p 8080:8080 -it <tag>
```

### Manual Acceptance Tests of generated archetype ###

#### Confirm ready - PASSING
curl 'http://localhost:8080/actuator/health/readiness'

#### Confirm GET works - PASSING
curl 'http://localhost:9000/api/hello'

#### Confirm POST works - PASSING
curl --location --request POST 'http://localhost:9000/api/greeting' \
--header 'Content-Type: application/json' \
--data-raw '{
"caller":"other"
}'

#### Confirm main exception handling works - PASSING
example: .throwException(new ArithmeticException())

#### Confirm RestException returns correctly - PASSING
curl --location --request POST 'http://localhost:9000/api/greeting' \
--header 'Content-Type: application/json' \
--data-raw '{
"wrongProp":"other"
}'

#### Confirm logs contain unique span and trace ids - FAILING
#### Confirm traces can be seen in Jaeger - FAILING

### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Jose Montoya, jmontoya@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com