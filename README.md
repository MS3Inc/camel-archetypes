# README #

This is a custom Maven archetype that generates a Camlel/SpringBoot application with stubs for API endpoints generated from a provided OpenAPI document.
It is part of the MS3 Integration Platform.

### How do I get set up? ###

Clone the develop branch of this repo.  Work should be done on branches with names like feature/MTK-nn, based on the MTK Jira project.

To build the archetype, CD into the archetype source and run "mvn clean install"

### How do I run the archetype? ###

To run the archetype, CD to the directory where the new project will live and then run:

mvn archetype:generate                                  \
-DarchetypeGroupId=com.ms3inc.camel               \
-DarchetypeArtifactId=oas-archetype          \
-DarchetypeVersion=0.1.0-SNAPSHOT                \
-DgroupId=com.ms3inc                                \
-DartifactId=test-java-app

You will be prompted for an API file.  Enter full path to the API file.  Do not use backslashes on a Windows computer. Convert to slashes:  c:/dev/myApi/src/main/resources/api/api.yaml

### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com