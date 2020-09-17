import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import java.lang.StringBuffer


def log = LoggerFactory.getLogger('org.apache.camel.archetype')
def encoding = 'UTF-8'

log.info "Generating RoutesGenerated.java"

def fileDir = Paths.get(request.outputDirectory, request.artifactId, 'src/main/java/com/ms3inc/camel').toFile()
def file = new File(fileDir, 'RoutesGenerated.java')
fileDir.mkdirs();

def buf = new StringBuffer();

// Rough out the routes for RoutesGenerated.java
buf.append('package com.ms3inc.camel;\n')
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
