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
@Grab(group="org.codehaus.groovy", module="groovy-all", version="2.4.8")
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
// |  COPY OPENAPI DOCUMENT
// |  Copy the provide OpenAPI document to the generated file system.
// |
// +-------------------------------------------------------------------------------------
log.info "\n==== Copying OpenAPI spec ===="

def oasPathStr = request.properties['specificationUri']
log.info ('API file to copy: ' + oasPathStr)
def oasFile = new File(oasPathStr)
StringBuffer oasBuf = new StringBuffer()

// Read the supplied OpenAPI document.
def oasReader = null
try {
	oasReader = new FileReader(oasFile)
	BufferedReader br = new BufferedReader(oasReader)
    for(String line; (line = br.readLine()) != null; ) 
    	oasBuf.append(line+'\n')
}
catch (FileNotFoundException nf) {
	log.error 'File not found: '+oasPathStr+' (convert backslashes to slashes)'
	//nf.printStackTrace()
}
catch (IOException io) {
	io.printStackStrace()
}
finally {
	if (oasReader != null)
		oasReader.close()
}

//  Create the sources/api directory in target file system.
def oasPath = FilePaths.get(request.outputDirectory, request.artifactId, 'src/generated/api/').toFile()
oasPath.mkdirs();
def fileName = oasFile.getName()
oasFile = new File(oasPath, fileName)

//	Write the OAS document.
log.info 'File to write: '+oasFile.getAbsolutePath()
def oasWriter = new FileWriter(oasFile)
oasWriter.write(oasBuf.toString())
oasWriter.close()




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
log.info "==== Add Endpoints to RoutesGenerated Class (rGen) ===="


// Read the RoutesGenerated placeholder file.
def rGenPath = request.outputDirectory + "/" + request.artifactId + '/src/generated/java/' + ((String) request.groupId).replaceAll("\\.", "/") + '/RoutesGenerated.java'
def rGenFile = new File (rGenPath)
def rGenBuf = new StringBuffer()

def rGenReader = null
try {
	rGenReader = new FileReader(rGenFile)
	BufferedReader br = new BufferedReader(rGenReader)
    for(String line; (line = br.readLine()) != null; ) 
    	rGenBuf.append(line+'\n')
}
catch (FileNotFoundException nf) {
	log.error 'File not found: '+rGenPath
}
catch (IOException io) {
	io.printStackStrace()
}
finally {
	if (rGenReader != null)
		rGenReader.close()
}

// Generate the routes code using API paths
rGenCode = new StringBuffer('rest()\n')
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
def rGenWriter = new FileWriter(rGenFile)
rGenWriter.write(rGenCodeStr)
rGenWriter.close()



// +-------------------------------------------------------------------------------------
// |
// |  ADD GENERATED CODE TO RoutesImplementation CLASS
// |  Load the placeholder file into memory, generate code, place it, and write it back out.
// |
// +-------------------------------------------------------------------------------------
log.info "==== Add Endpoints to RoutesImplemented (rImp) Class ===="

// Read the RoutesImplemented placeholder file.
def rImpPath = request.outputDirectory + "/" + request.artifactId + '/src/main/java/' + ((String) request.groupId).replaceAll("\\.", "/") + '/RoutesImplementation.java'
def rImpFile = new File (rImpPath)
def rImpBuf = new StringBuffer()

def rImpReader = null
try {
	rImpReader = new FileReader(rImpFile)
	BufferedReader br = new BufferedReader(rImpReader)
    for(String line; (line = br.readLine()) != null; )
    	rImpBuf.append(line+'\n')
}
catch (FileNotFoundException nf) {
	log.error 'File not found: '+rImpPath
}
catch (IOException io) {
	io.printStackStrace()
}
finally {
	if (rImpReader != null)
		rImpReader.close()
}

//  Generate code using the opIdList.
rGenCode = new StringBuffer()
for (String opId : opIdList) {
	rGenCode.append('from("direct:'+opId+'")\n')
    rGenCode.append('\t\t\t\t\t.to("direct:util:setCurrentRouteInfo")\n')
    // can't have two routes with the same route id, in this case the same as given to the rest routes
	// rGenCode.append('\t\t\t\t\t.routeId("'+opId+'")\n')
    rGenCode.append('\t\t\t\t\t.log("Start of ${exchangeProperty.currentRoute}")\n')
    rGenCode.append('\t\t\t\t\t.setBody(datasonnet("{hello: \'world\'}", String.class).outputMediaType("application/json"))\n')
    rGenCode.append('\t\t\t\t\t.log("End of ${exchangeProperty.currentRoute}")\n')

	rGenCode.append('\t\t\t\t;\n')
}

//	Write the RoutesImplemented document.
def rImpCodeStr = rImpBuf.toString().replace ('[generated-routes]', rGenCode.toString())
log.info 'File to write: '+rImpFile.getAbsolutePath()
def rImpWriter = new FileWriter(rImpFile)
rImpWriter.write(rImpCodeStr)
rImpWriter.close()
