package com.github.tonivade.buildtiful

import org.apache.tools.ant.Task
import org.apache.tools.ant.taskdefs.Javac
import org.apache.tools.ant.types.Path
import org.apache.tools.ant.{Project => AntProject}
import java.io.File

trait Compiler {
  def compile(build: Build) : Unit
}

class DefaultCompiler extends Compiler {
  val project : AntProject = {
    val project = new AntProject()
    project.setBaseDir(new File("target/buildtiful"));
    project.init() 
    project
  }
  
  def compile(build: Build) {
    println("compile")
    
    javac(build).execute()
  }
  
  def javac(build: Build): Task = {
    val javac = new Javac()
    javac.setProject(project)
    build.sources.main.map(src => new Path(project, src)).map(javac.setSrcdir(_))
    build.sources.test.map(src => new Path(project, src)).map(javac.setSrcdir(_))
    
    val classpath = new Path(project)
    classpath.setPath("target/buildtiful/libs")
    javac.setClasspath(classpath)
    
    javac
  }
}