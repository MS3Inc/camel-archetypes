# README #

Camel has a basic Maven archetype.  The OpenAPI component has a POM that includes a generate goal.  This archetype will merges these into a single archetype that will generate all of the files needed to create a Camel application to handle endopints defined by an OpenAPI document.

### How do I get set up? ###

Clone the develop branch of this repo.  Work should be done on branches with names like feature/MTK-nn, based on the MTK Jira project.

To build the archetype, CD into the archetype source and run "mvn clean install"

### How do I run the archetype? ###

To run the archetype, CD to the directory where the new project will live and then run:

mvn archetype:generate                                  \
-DarchetypeGroupId=com.ms3inc                \
-DarchetypeArtifactId=java-proto-archetype          \
-DarchetypeVersion=0.0.1-SNAPSHOT                \
-DgroupId=com.ms3inc                                \
-DartifactId=test-java-app

You will be prompted for an API file.  Enter full path to the API file.  Do not use backslashes on a Windows computer. Convert to slashes:  c:/dev/myApi/src/main/resources/api/api.yaml

### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com