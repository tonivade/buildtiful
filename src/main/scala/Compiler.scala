package com.github.tonivade.buildtiful

import org.apache.tools.ant.Task
import org.apache.tools.ant.taskdefs.Javac
import org.apache.tools.ant.types.Path
import org.apache.tools.ant.types.FileSet
import java.io.File

import Config._

trait Compiler {
  def compile(build: Build) : Unit
}

class DefaultCompiler extends Compiler {
  
  def compile(build: Build) {
    println("compile")
    
    if (!classes.exists()) classes.mkdirs()
    
    javac(build).execute()
  }
  
  def javac(build: Build): Task = {
    val javac = new Javac()
    javac.setProject(project)
    build.sources.main.map(src => new Path(project, src)).map(javac.setSrcdir(_))
    build.sources.test.map(src => new Path(project, src)).map(javac.setSrcdir(_))
    javac.setClasspath(classpath(build))
    javac.setDestdir(classes)
    javac
  }
  
  def classpath(build: Build): Path = {
    val classpath = new Path(project)
    val fileset = new FileSet()
    fileset.setDir(libs)
    fileset.setIncludes("*.jar")
    classpath.addFileset(fileset)
    classpath
  }
}