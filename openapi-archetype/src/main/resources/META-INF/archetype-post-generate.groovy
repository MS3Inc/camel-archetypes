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
import org.slf4j.LoggerFactory

def log = LoggerFactory.getLogger('org.apache.camel.archetype')
log.info("Attempting to execute mvn command...\n")

def specUri = request.properties['specificationUri']
def generatedApiDirectory = request.outputDirectory + "/" + request.artifactId

def mvnCommand = "mvn com.ms3-inc.camel:camel-restdsl-openapi-plugin:1.0-SNAPSHOT:generate -DspecificationUri=" + specUri + " -f " + generatedApiDirectory + " -X"
log.info mvnCommand.execute().text