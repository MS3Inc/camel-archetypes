@Grab(group='io.swagger.parser.v3', module='swagger-parser', version='2.0.21')

import java.util.Map
import java.util.Set
import java.io.*
import java.net.URL
import java.nio.file.Paths
import java.lang.StringBuffer

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
//import io.swagger.v3.oas.models.Paths
  
def log = LoggerFactory.getLogger('org.apache.camel.archetype')
def encoding = 'UTF-8'



log.info "==== Copying OpenAPI spec ===="

def oasPathStr = request.properties['oasSpecFile']
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
def oasPath = Paths.get(request.outputDirectory, request.artifactId, 'src/main/resources/api/').toFile()
oasPath.mkdirs();
def fileName = oasFile.getName()
oasFile = new File(oasPath, fileName)

//	Write the OAS document.
log.info 'File to write: '+oasFile.getAbsolutePath()
def oasWriter = new FileWriter(oasFile)
oasWriter.write(oasBuf.toString())
oasWriter.close()



log.info "==== Add Endpoints to RoutesGenerated Class ===="

// Parse the OAS document.
OpenAPI openAPI = new OpenAPIV3Parser().read(oasPathStr)

// Read the RoutesGenerated placeholder file.
def rGenPath = request.outputDirectory + "/" + request.artifactId + '/src/main/java/com/ms3inc/camel/RoutesGenerated.java'
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
	log.error 'File not found: '+rGenPathStr
}
catch (IOException io) {
	io.printStackStrace()
}
finally {
	if (rGenReader != null)
		rGenReader.close()
}



// Generate the routes code
rGenCode = new StringBuffer('\trest()\n')
io.swagger.v3.oas.models.Paths paths = openAPI.getPaths();
Set<String> pathKeys = paths.keySet();

for (String path : pathKeys) {
	PathItem item = paths.get((Object)path);
	rGenCode.append('\n')
	
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
		def desc = getOp.getDescription()
		rGenCode.append('\t\t.get("' + path + '")\n')
		rGenCode.append('\t\t\t.id("' + opId + '")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("' + desc + '")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:' + opId + '")\n')
	}
	if (putOp != null) {
		def opId = 'put'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = putOp.getDescription()
		rGenCode.append('\t\t.put("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
	if (postOp != null) {
		def opId = 'post'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = postOp.getDescription()
		rGenCode.append('\t\t.post("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
	if (deleteOp != null) {
		def opId = 'delete'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = deleteOp.getDescription()
		rGenCode.append('\t\t.delete("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
	if (patchOp != null) {
		def opId = 'patch'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = patchOp.getDescription()
		rGenCode.append('\t\t.patch("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
	if (headOp != null) {
		def opId = 'head'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = headOp.getDescription()
		rGenCode.append('\t\t.head("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
	if (optionsOp != null) {
		def opId = 'options'+path.replace('/', '_')
		opId = opId.replace('{', '')
		def desc = optionsOp.getDescription()
		rGenCode.append('\t\t.options("'+path+'")\n')
		rGenCode.append('\t\t\t.id("'+opId+'")\n')
		if (desc != null)
			rGenCode.append('\t\t\t.description("'+desc+'")\n')
		rGenCode.append('\t\t\t.produces("application/json")\n')
		rGenCode.append('\t\t\t.to("direct:'+opId+'")\n')
	}
}
rGenCode.append('\t;\n')

//	Write the OAS document.
def rGenCodeStr = rGenBuf.toString().replace ('INSERT-CODE-HERE', rGenCode.toString())

//System.out.println('\n\n'+rGenCodeStr.toString())


log.info 'File to write: '+rGenFile.getAbsolutePath()
def rGenWriter = new FileWriter(rGenFile)
rGenWriter.write(rGenCodeStr)
rGenWriter.close()




//def parserClassName = classLoader.loadClass("io.swagger.v3.oas.models.OpenAPI").getName()
//System.out.println('Parser Class: '+parserClassName)
//def parser = new parserClassName()

//def packageStr = request.properties['package']
//def packageDir = packageStr.replace(".", "/")
//log.info 'Directory form of the package:  '+packageDir

//def fileDir = Paths.get(request.outputDirectory, request.artifactId, 'src/main/java/'+packageDir).toFile()
//def file = new File(fileDir, 'RoutesGenerated.java')
//fileDir.mkdirs();

def buf = new StringBuffer();

// Rough out the routes for RoutesGenerated.java  This is a cumbersome approach.  Replace it with 
// a pre-bulit file with a place holder for the generated code.  Inject new code at that point>
//buf.append('package '+packageStr+';\n')
buf.append('\n')
buf.append('import javax.annotation.Generated;\n')
buf.append('import org.apache.camel.builder.RouteBuilder;\n')
buf.append('import org.springframework.stereotype.Component;\n')
buf.append('\n')
buf.append('@Generated("com.ms3inc.camel-oas-archetype")\n')
buf.append('@Component\n')
buf.append('public class RoutesGenerated extends RouteBuilder {\n')
buf.append('\n')
buf.append('\tpublic void configure() {\n')
buf.append('\t}\n')
buf.append('\n')
buf.append('}\n')

// Write generated code to RoutesGenerated.java
//def writer = new FileWriter(file)
//writer.write(buf.toString())
//writer.close()
