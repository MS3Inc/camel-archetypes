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
//  This code takes a brute force approach to generating Camel code from an OpenAPI document.
//  Groovy does support the creation of classes, even in their own files.
//  At some point, we may want to refactor this code to make it simpler, with classes that avoid code duplication.
//  - mnorton@ms3inc.com

//  Groovy doesn't use the classpath of the containing application, the camel.oas.archetype in this case.
//  The following annotations uses the Grape/Ivy Groovy dependency manager to bring in the OpenAPI parser.
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
def codeGenRead (String readFileName) {
	def cgLog = LoggerFactory.getLogger('org.apache.camel.archetype')
	cgLog.info ''
	cgLog.info '==== Read Code Gen Template File  ===='
	cgLog.info 'Template file: '+readFileName
	
	new File(readFileName).getText()
}

def codeGenWrite (String writeFileName, String code) {
}

def tabs(int level) {
	def sb = new StringBuffer()
	for (int i=0; i<level; i++)
		sb.append('\t')
	return sb.toString()
}

//def baseDir = getProperty('user.dir')
//def tempSb = codeGenRead(baseDir + File.separator + 'codeGenTemplate.txt')
//log.info 'Template file read:'
//System.out.println tempSb.toString()




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
rGenCode = new StringBuffer(tabs(indent)+'rest()\n')
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
		def opId = 'get'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = getOp.getDescription()
		rGenCode.append('\t\t\t.get("' + path + '")\n')
		rGenCode.append('\t\t\t\t.id("' + opId + '")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("' + desc + '")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:' + opId + '")\n')
	}
	if (putOp != null) {
		def opId = 'put'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = putOp.getDescription()
		rGenCode.append('\t\t\t.put("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
	if (postOp != null) {
		def opId = 'post'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = postOp.getDescription()
		rGenCode.append('\t\t\t.post("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
	if (deleteOp != null) {
		def opId = 'delete'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = deleteOp.getDescription()
		rGenCode.append('\t\t\t.delete("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
	if (patchOp != null) {
		def opId = 'patch'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = patchOp.getDescription()
		rGenCode.append('\t\t\t.patch("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
	if (headOp != null) {
		def opId = 'head'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = headOp.getDescription()
		rGenCode.append('\t\t\t.head("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
	if (optionsOp != null) {
		def opId = 'options'+path.replace('/', '_')
		opId = opId.replace('{', '')
		opIdList.add(opId)
		def desc = optionsOp.getDescription()
		rGenCode.append('\t\t\t.options("'+path+'")\n')
		rGenCode.append('\t\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t\t.to("direct:'+opId+'")\n')
	}
}
rGenCode.append('\t\t;')

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
	rGenCode.append('from("direct:'+opId+'")\n')
    rGenCode.append('\t\t\t\t\t.to("direct:util:setCurrentRouteInfo")\n')
    // can't have two routes with the same route id, in this case the same as given to the rest routes
	// rGenCode.append('\t\t\t\t\t.routeId("'+opId+'")\n')
    rGenCode.append('\t\t\t\t\t.log("Start of ${exchangeProperty.currentRoute}")\n')
    rGenCode.append('\t\t\t\t\t.setBody(datasonnet("{opId: \'')
    rGenCode.append(opId)
    rGenCode.append('\'}", String.class).outputMediaType("application/json"))\n')
    rGenCode.append('\t\t\t\t\t.log("End of ${exchangeProperty.currentRoute}")\n')

	rGenCode.append('\t\t\t\t;\n')
}

//	Write the RoutesImplemented document.
def rImpCodeStr = rImpBuf.toString().replace ('[generated-routes]', rGenCode.toString())
log.info 'File to write: '+rImpFile.getAbsolutePath()
rImpFile.write(rImpCodeStr)

//def rImpWriter = new FileWriter(rImpFile)
//rImpWriter.write(rImpCodeStr)
//rImpWriter.close()
