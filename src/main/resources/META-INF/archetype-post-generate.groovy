System.out.println '***********************  BOOT STRAP PACKAGES  ***************************'

//	Be aware that the the ClassLoader is actually a GroovyClassLoader, with additional methods.  Inherits the rest.
def classLoader = this.class.getClassLoader()
System.out.println ("Class Loader:  "+classLoader.class.getName())
this.class.getClassLoader().addClasspath('./lib/swagger-parser-1.0.51.jar')

def parser = this.class.getClassLoader().loadClass('io.swagger.v3.oas.models.OpenAPI')

//def swaggerUrl = new URL('file://./lib/swagger-parser-1.0.51.jar')
//classLoader.definePackage("swagger-parser", "OpenAPI Parser", "2.0.21", "io.swagger.parser.v3", "OpenAPI Parser", "2.0.21", "io.swagger.parser.v3", swaggerUrl)
//System.out.println(this.class.getClassLoader().class.getMethods().toString().replace(',','\n'))
//System.out.println(this.class.getClassLoader().getPackages().toString().replace(',','\n'))

//System.out.println(this.getClassLoader().getClassPath().toString())

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Map;
import java.util.Set;
import java.io.File
import java.io.FileWriter
import java.net.URL
import java.nio.file.Paths
import java.lang.StringBuffer
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.Operation;
//import io.swagger.v3.oas.models.PathItem;
//import io.swagger.v3.oas.models.Paths;

def log = LoggerFactory.getLogger('org.apache.camel.archetype')
def encoding = 'UTF-8'


log.info "==== Copying OpenAPI spec ===="
def oasPathStr = request.properties['oas-spec-file']
def oasPath = Paths.get(request.outputDirectory, request.artifactId, 'src/main/resources/api/').toFile()
def oasFile = new File(oasPath, 'api.yaml')
oasPath.mkdirs();
def oasWriter = new FileWriter(oasFile)
// Read the OAS spec here.
oasWriter.write("spec goes here")
oasWriter.close()

log.info "==== Generating Route Stubs ===="

//def parserClassName = classLoader.loadClass("io.swagger.v3.oas.models.OpenAPI").getName()
//System.out.println('Parser Class: '+parserClassName)
//def parser = new parserClassName()

def packageStr = request.properties['package']
def packageDir = packageStr.replace(".", "/")
log.info 'Directory form of the package:  '+packageDir

def fileDir = Paths.get(request.outputDirectory, request.artifactId, 'src/main/java/'+packageDir).toFile()
def file = new File(fileDir, 'RoutesGenerated.java')
fileDir.mkdirs();

def buf = new StringBuffer();

// Rough out the routes for RoutesGenerated.java  This is a cumbersome approach.  Replace it with 
// a pre-bulit file with a place holder for the generated code.  Inject new code at that point>
buf.append('package '+packageStr+';\n')
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
def writer = new FileWriter(file)
writer.write(buf.toString())
writer.close()
