# README #

This is a Maven archetype that generates a Camel/SpringBoot application with stubs for API endpoints generated from a provided OpenAPI document.
It is part of MS3's integration platform, [Tavros](https://github.com/MS3Inc/tavros).

### Migration recommendations ###

There is currently no way to update an API that has already been created with the archetype. The recommended way to update existing applications is to regenerate the API using the same specification in a different folder and then use a diff tool such as Beyond Compare to migrate changes. It will likely be easier to move the newly generated code to the previously generated code but it largely depends on what the diff is.

### How do I get set up? ###

This version of the archetypes is currently being released as a snapshot on Github. The camel-restdsl-openapi-plugin and camel-rest-extensions have also been released on GitHub to support this. Follow the below instructions to get set up.

1. If you have previously installed the archetype, camel-rest-extensions, camel-restdsl-openapi-plugin locally, delete them from your .m2 repo inside of the com.ms3-inc.tavros folder.
2. Create or login to an existing GitHub account.
3. Create [a classic Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic) with ONLY `read:packages` scope necessary
4. Modify your settings.xml with the values below, and replace the username and password fields with your GitHub credentials.
```
  <servers>
    <server>
      <!-- ID matching repo in generated API from archetype -->
      <id>github-ms3-extensions</id>
        <username>your github username</username>
        <password>your PAT created above</password>
    </server>
    <server>
      <id>archetype</id>
        <username>your github username</username>
        <password>your PAT created above</password>
    </server>
    <server>
      <id>github-plugins</id>
        <username>your github username</username>
        <password>your PAT created above</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>archetype</id>
          <url>https://maven.pkg.github.com/ms3inc/camel-archetypes</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
              <enabled>true</enabled>
          </snapshots>
      </repository>
    </repositories>
    <pluginRepositories>
      <pluginRepository>
        <id>github-plugins</id>
        <snapshots>
          <enabled>true</enabled>
        </snapshots>
        <releases>
          <enabled>true</enabled>
        </releases>
        <url>https://maven.pkg.github.com/ms3inc/camel-restdsl-openapi-plugin</url>
      </pluginRepository>
    </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
```

Once those steps are complete, you can move on to the next step of running the archetype.

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

### To allow the OTEL logging

The `maven-dependency-plugin` config is what moves the OTEL agent to the target folder.

The `configuration.agents.agent` in the `spring-boot-maven-plugin` is what allows `mvn spring-boot:run` to start with the OTEL agent.

In JVM arguments in your IDE or deploed, configure `-javaagent:target/javaagents/javaagent.jar` allow the agent to start along with SB.

### Who do I talk to? ###

Contact:

* Mark Norton, mnorton@ms3-inc.com
* Jose Montoya, jmontoya@ms3-inc.com
* Rob Ratcliffe, rratcliffe@ms3-inc.com
