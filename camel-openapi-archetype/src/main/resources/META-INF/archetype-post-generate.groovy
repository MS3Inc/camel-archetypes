/**
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

def log = LoggerFactory.getLogger('org.apache.camel.archetype')

boolean isFailure = false;

File readme = Paths.get(request.outputDirectory, request.artifactId, "README.md").toFile()
String replacedReadme = readme.text.replace('${archetypeVersion}', request.archetypeVersion)
readme.text = replacedReadme

Path origGitIgnorePath = Paths.get(request.outputDirectory, request.artifactId, "gitignore");
Path newGitIgnorePath = origGitIgnorePath.getParent().resolve(".gitignore");

if (Files.exists(origGitIgnorePath)) {
    try {
        Files.move(origGitIgnorePath, newGitIgnorePath);
        log.info("Created .gitignore.\n")
    } catch (Exception e) {
        log.error("Could not create .gitignore file. " + e.getMessage().toString())
        isFailure = true;
    }
} else {
    log.error("Could not find $origGitIgnorePath")
    isFailure = true;
}

String specUri = request.properties['specificationUri']
specUri = specUri.trim()

def isSample = specUri == "greeting-sample.yaml"
String sampleSpecPath = request.outputDirectory + "/"+ request.artifactId +  "/src/main/resources/greeting-sample.yaml";

if (isSample) {
    specUri = sampleSpecPath;
}

def generatedApiDirectory = request.outputDirectory + "/" + request.artifactId
def camelRestDslPluginVersion = '0.1.6'
def mvnCommand = ['mvn', 'com.ms3-inc.tavros:camel-restdsl-openapi-plugin:' + camelRestDslPluginVersion + ':generate', '-DspecificationUri=' + specUri, '-f', generatedApiDirectory]

if (Files.exists(Path.of(specUri))) {
    log.info("Attempting to generate routes from specification...\n")

    log.info("Running OpenAPI plugin in " + generatedApiDirectory + "...")

    def prefixForRunningWithWindows =  ['cmd', '/c']
    if (System.properties['os.name'].toLowerCase().contains('windows')) {
        log.info "Executing command for Windows..."
        mvnCommand = prefixForRunningWithWindows + mvnCommand
    }

} else {
    log.error("File doesn't exist. Please try again.")
    isFailure = true;
}

Process process = mvnCommand.execute()
def out = new StringBuffer()
def err = new StringBuffer()
process.consumeProcessOutput( out, err )
process.waitFor()
if( out.size() > 0 ) println out
if( err.size() > 0 ) println err

if (process.exitValue() != 0) {
    log.error("There was an error with the OpenAPI plugin.")
    isFailure = true;
}

if (isFailure) {
    throw new Exception("Build stopped because the post processing script ran into a problem, see details above.");
} else {
    log.info "Cleaning up sample spec file..."
    Files.deleteIfExists(Path.of(sampleSpecPath));
}
