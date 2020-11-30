# README #

This is a custom Maven archetype that generates a Camlel/SpringBoot application with stubs for API endpoints generated from a provided OpenAPI document.
It is part of the MS3 Integration Platform.

### How do I get set up? ###

Clone the develop branch of this repo.  Work should be done on branches with names like feature/MTK-nn, based on the MTK Jira project.

To build the archetype, CD into the archetype source and run "mvn clean install"

### How do I run the archetype? ###

To run the archetype, CD to the directory where the new project will live and then run:

mvn archetype:generate \\  
-DarchetypeGroupId=com.ms3-inc.camel \\  
-DarchetypeArtifactId=openapi-archetype \\  
-DarchetypeVersion=0.1.1-SNAPSHOT \\  
-DpackageInPathFormat \\  
-Dpackage=com.ms3_inc.camel \\  
-DgroupId=com.ms3-inc.camel \\   
-Dversion=0.0.1-SNAPSHOT \\  
-DartifactId=CHANGE-TO-API-NAME

The -DpackageInPathFormat and -package arguments are only needed if your groupId has a dash in it, such as com.ms3-inc.  Leave them out if not needed.

You will be prompted for an API file.  Enter full path to the API file.  Do not use backslashes on a Windows computer. Convert to slashes:  c:/dev/myApi/src/main/resources/api/api.yaml.  This can also be provided on the command line using -DspecificationUri=PATH

### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com