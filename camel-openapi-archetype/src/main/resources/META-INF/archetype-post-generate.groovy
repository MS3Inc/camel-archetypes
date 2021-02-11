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

Path origGitIgnorePath = Paths.get(request.outputDirectory, request.artifactId, "gitignore");
Path newGitIgnorePath = origGitIgnorePath.getParent().resolve(".gitignore");

if (Files.exists(origGitIgnorePath)) {
    try {
        Files.move(origGitIgnorePath, newGitIgnorePath);
        log.info("Created .gitignore...\n")
    } catch (Exception e) {
        log.error("Could not create .gitignore file. " + e.getMessage().toString())
        isFailure = true;
    }
} else {
    log.error("Could not find $origGitIgnorePath")
    isFailure = true;
}



log.info("Attempting to execute mvn command...\n")

def specUri = request.properties['specificationUri']
def generatedApiDirectory = request.outputDirectory + "/" + request.artifactId

log.info("Running mvn in " + generatedApiDirectory)

def prefixForRunningWithWindows =  ['cmd', '/c']
def mvnCommand = ['mvn', 'com.ms3-inc.tavros:camel-restdsl-openapi-plugin:0.1.6:generate', '-DspecificationUri=' + specUri, '-f', generatedApiDirectory]

if (System.properties['os.name'].toLowerCase().contains('windows')) {
    log.info "Executing command for Windows"
    mvnCommand = prefixForRunningWithWindows + mvnCommand
}

Process process = mvnCommand.execute()
def out = new StringBuffer()
def err = new StringBuffer()
process.consumeProcessOutput( out, err )
process.waitFor()
if( out.size() > 0 ) println out
if( err.size() > 0 ) println err

if (process.exitValue() != 0) {
    isFailure = true;
}

if (isFailure) {
    throw new Exception("Build stopped because the post processing script ran into a problem.");
}
