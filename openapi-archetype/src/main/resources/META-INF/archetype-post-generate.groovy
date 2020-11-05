/**
 * Copyright 2020 the original author or authors.
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
 
 
//  This code takes a brute force approach to generating Camel code from an OpenAPI document, largely due to
//  how the Swagger parser works.
//
//  - mnorton@ms3inc.com

//  Groovy doesn't use the classpath of the containing application, the camel.oas.archetype in this case.
//  The following annotations uses the Grape/Ivy Groovy dependency manager to bring in the OpenAPI parser.
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse

@Grab(group='io.swagger.parser.v3', module='swagger-parser', version='2.0.21')


import java.nio.file.Paths as FilePaths

import org.slf4j.LoggerFactory
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths

def log = LoggerFactory.getLogger('org.apache.camel.archetype')
def encoding = 'UTF-8'


// +-------------------------------------------------------------------------------------
// |
// |  UTILITY FUNCTIONS
// |  Functions that capture reusable script segments.
// |
// +-------------------------------------------------------------------------------------
def tabs(int level) {
	def sb = new StringBuffer()
	for (int i=0; i<level; i++)
		sb.append('\t')
	return sb.toString()
}



// +-------------------------------------------------------------------------------------
// |
// |  COPY OPENAPI DOCUMENT
// |  Copy the provide OpenAPI document to the generated file system.
// |
// +-------------------------------------------------------------------------------------
log.info ''
log.info '==== Copying OpenAPI spec ===='

def oasPathStr = request.properties['specificationUri']
log.info ('API file to copy: ' + oasPathStr)
def oasFile = new File(oasPathStr)
def specText = oasFile.getText();

//  Create the sources/api directory in target file system.
def oasPath = FilePaths.get(request.outputDirectory, request.artifactId, 'src/generated/api/').toFile()
oasPath.mkdirs();
def fileName = oasFile.getName()
oasFile = new File(oasPath, fileName)
log.info 'File to write: '+oasFile.getAbsolutePath()

oasFile.write(specText)




// +-------------------------------------------------------------------------------------
// |
// |  CREATE AN OPENAPI PARSER AND PARSE THE DOCUMENT
// |  Parse the OpenAPI document given by oasPathStr (passed as a required property)
// |
// +-------------------------------------------------------------------------------------
OpenAPI openAPI = new OpenAPIV3Parser().read(oasPathStr)




// +-------------------------------------------------------------------------------------
// |
// |  ADD GENERATED CODE TO RoutesGenerated CLASS
// |  Load the placeholder file into memory, generate code, place it, and write it back out.
// |
// +-------------------------------------------------------------------------------------
log.info ''
log.info '==== Add Endpoints to RoutesGenerated Class (rGen) ===='


// Read the RoutesGenerated placeholder file.
def rGenPath = request.outputDirectory + "/" + request.artifactId + '/src/generated/java/' + ((String) request.groupId).replaceAll("\\.", "/").replaceAll("-", "_") + '/RoutesGenerated.java'
def rGenFile = new File (rGenPath)
def rGenBuf = new StringBuffer(rGenFile.getText())

// -----------------------------------------
// Generate the routes code using API paths
// -----------------------------------------
def indent = 2
rGenCode = new StringBuffer()

//	Add support for request validation.
rGenCode.append(tabs(indent)+'interceptFrom()\n')
indent = 3
rGenCode.append(tabs(indent)+'.process(new OpenApi4jValidator("'+fileName+'", "/api"));\n\n')

//  Add start of rest endpoints.
indent = 2
rGenCode.append(tabs(indent)+'rest()\n')

Paths paths = openAPI.getPaths();
Set<String> pathKeys = paths.keySet();

def opIdList = new Vector<String>()

for (String path : pathKeys) {
	PathItem item = paths.get((Object)path);
	
	// List<Operation> ops = item.readOperations();
	Map<PathItem.HttpMethod,Operation> ops = item.readOperationsMap();
	
	Operation getOp = item.getGet();
	Operation putOp = item.getPut();
	Operation postOp = item.getPost();
	Operation deleteOp = item.getDelete();
	Operation patchOp = item.getPatch();
	Operation headOp = item.getHead();
	Operation optionsOp = item.getOptions();
	
	if (getOp != null) {
		indent=3
		def opId = 'get'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('}', '')
		opIdList.add(opId)
		def desc = getOp.getDescription()
		rGenCode.append(tabs(indent)+'.get("' + path + '")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("' + opId + '")\n')

		List<String> produces = new ArrayList<>()
		getOp.getResponses().forEach{status, resp ->
				resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:' + opId + '")\n')
	}
	if (putOp != null) {
		indent=3
		def opId = 'put'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('}', '')
		opIdList.add(opId)
		def desc = putOp.getDescription()
		rGenCode.append(tabs(indent)+'.put("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')

		List<String> consumes = new ArrayList<>()
		putOp.getRequestBody().getContent().forEach{mediaType, mediaTypeObj -> consumes.add(mediaType)}
		if (consumes.size() > 0) {
			rGenCode.append(tabs(indent)+'.consumes("' + String.join(",", consumes) + '")\n')
		}

		List<String> produces = new ArrayList<>()
		putOp.getResponses().forEach{status, resp ->
			resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
	if (postOp != null) {
		indent=3
		def opId = 'post'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('}', '')
		opIdList.add(opId)
		def desc = postOp.getDescription()
		rGenCode.append(tabs(indent)+'.post("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')

		List<String> consumes = new ArrayList<>()
		postOp.getRequestBody().getContent().forEach{mediaType, mediaTypeObj -> consumes.add(mediaType)}
		if (consumes.size() > 0) {
			rGenCode.append(tabs(indent)+'.consumes("' + String.join(",", consumes) + '")\n')
		}

		List<String> produces = new ArrayList<>()
		postOp.getResponses().forEach{status, resp ->
			resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
	if (deleteOp != null) {
		indent=3
		def opId = 'delete'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('}', '')
		opIdList.add(opId)
		def desc = deleteOp.getDescription()
		rGenCode.append(tabs(indent)+'.delete("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')

		List<String> produces = new ArrayList<>()
		deleteOp.getResponses().forEach{status, resp ->
			resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
	if (patchOp != null) {
		indent=3
		def opId = 'patch'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('{', '')
		opIdList.add(opId)
		def desc = patchOp.getDescription()
		rGenCode.append(tabs(indent)+'.patch("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')

		List<String> consumes = new ArrayList<>()
		patchOp.getRequestBody().getContent().forEach{mediaType, mediaTypeObj -> consumes.add(mediaType)}
		if (consumes.size() > 0) {
			rGenCode.append(tabs(indent)+'.consumes("' + String.join(",", consumes) + '")\n')
		}

		List<String> produces = new ArrayList<>()
		patchOp.getResponses().forEach{status, resp ->
			resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
	if (headOp != null) {
		indent=3
		def opId = 'head'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('{', '')
		opIdList.add(opId)
		def desc = headOp.getDescription()
		rGenCode.append(tabs(indent)+'.head("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')
		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
	if (optionsOp != null) {
		indent=3
		def opId = 'options'+path.replace('/', '_')
		opId = opId.replace('{', '').replace('{', '')
		opIdList.add(opId)
		def desc = optionsOp.getDescription()
		rGenCode.append(tabs(indent)+'.options("'+path+'")\n')
		indent=4
		rGenCode.append(tabs(indent)+'.id("'+opId+'")\n')

		List<String> produces = new ArrayList<>()
		getOp.getResponses().forEach{status, resp ->
			resp.getContent().forEach{mediaType, mediaTypeObj -> produces.add(mediaType)}}
		if (produces.size() > 0) {
			rGenCode.append(tabs(indent)+'.produces("' + String.join(",", produces) + '")\n')
		}

		rGenCode.append(tabs(indent)+'.to("direct:'+opId+'")\n')
	}
}
indent=2
rGenCode.append(tabs(indent)+';')

//	Write the RoutesGenerated document.
def rGenCodeStr = rGenBuf.toString().replace ('[generated-restdsl]', rGenCode.toString())
log.info 'File to write: '+rGenFile.getAbsolutePath()
rGenFile.write(rGenCodeStr)




// +-------------------------------------------------------------------------------------
// |
// |  ADD GENERATED CODE TO RoutesImplementation CLASS
// |  Load the placeholder file into memory, generate code, place it, and write it back out.
// |
// +-------------------------------------------------------------------------------------
log.info ''
log.info '==== Add Endpoints to RoutesImplemented (rImp) Class ===='

// Read the RoutesImplemented placeholder file.
def rImpPath = request.outputDirectory + "/" + request.artifactId + '/src/main/java/' + ((String) request.groupId).replaceAll("\\.", "/").replaceAll("-", "_") + '/RoutesImplementation.java'
def rImpFile = new File (rImpPath)
def rImpBuf = new StringBuffer(rImpFile.getText())

//  ------------------------------------
//  Generate code using the opIdList.
//  ------------------------------------
rGenCode = new StringBuffer()
for (String opId : opIdList) {
	indent=2
	rGenCode.append(tabs(indent)+'from("direct:'+opId+'")\n')
	indent=3
    rGenCode.append(tabs(indent)+'.to("direct:util:setCurrentRouteInfo")\n')
    rGenCode.append(tabs(indent)+'.setBody(datasonnet("{opId: \'')
    rGenCode.append(opId)
    rGenCode.append('\'}", String.class).outputMediaType("application/json"))\n')

	indent=2
	rGenCode.append(tabs(indent)+';\n')
}

//	Write the RoutesImplemented document.
def rImpCodeStr = rImpBuf.toString().replace ('[generated-routes]', rGenCode.toString())
log.info 'File to write: '+rImpFile.getAbsolutePath()
rImpFile.write(rImpCodeStr)

