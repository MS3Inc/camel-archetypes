# README #

This is a Maven archetype that generates a Camel/SpringBoot application with stubbed code for a scheduled (batch) task.
It is part of the MS3 Integration Platform.

### How do I get set up? ###

Clone the develop branch of this repo.  Work should be done on branches with names like feature/MTK-nn, based on the MTK Jira project.

To build the archetype, CD into the archetype source and run "mvn clean install"

### How do I run the archetype? ###

To run the archetype, CD to the directory where the new project will live and then run:

```bash
mvn archetype:generate \\  
-DarchetypeGroupId=com.ms3-inc.tavros \\  
-DarchetypeArtifactId=camel-scheduled-archetype \\  
-DarchetypeVersion=<check-for-latest>
```

You will be prompted for a `package`, `groupId`,` version`, and `artifactId` (project name).

The `-DpackageInPathFormat` and `-package` arguments should be supplied if your groupId has a dash in it, such as com.ms3-inc.  Leave them out if not needed. Here is an example:
```bash
-DpackageInPathFormat \\  
-Dpackage=com.ms3_inc.tavros \\  
-DgroupId=com.ms3-inc.tavros \\  
-Dversion=0.0.1-SNAPSHOT
```


### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Jose Montoya, jmontoya@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com
