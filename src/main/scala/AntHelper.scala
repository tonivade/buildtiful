package com.github.tonivade.buildtiful

import org.apache.tools.ant.{ Project => AntProject }
import org.apache.tools.ant.Task
import org.apache.tools.ant.taskdefs.Javac
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement.TypeAttribute
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask.SummaryAttribute
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path

import Config._

object AntHelper {
    
  val project : AntProject = {
    val project = new AntProject()
    project.setBaseDir(baseDir)
    project.init() 
    project
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
  
    
  def junit(build: Build): Task = {
    val junit = new JUnitTask()
    val printsummary = new SummaryAttribute();
    printsummary.setValue("true")
    junit.setPrintsummary(printsummary)
    junit.setProject(project)

    val classpath = junit.createClasspath();
    val fileset = new FileSet()
    fileset.setDir(libs)
    fileset.setIncludes("*.jar")
    classpath.addFileset(fileset)
    classpath.add(new Path(project, path("/target/classes")))
    
    val tests = junit.createBatchTest()
    tests.setFork(false)
    tests.setTodir(report)
    val testsFileSet = new FileSet()
    testsFileSet.setDir(file("/target/classes"))
    testsFileSet.setIncludes("**/*Test.class")
    tests.addFileSet(testsFileSet)
    
    val formatter = new FormatterElement()
    val formatterType = new TypeAttribute()
    formatterType.setValue("plain")
    formatter.setType(formatterType)
    junit.addFormatter(formatter)
    junit
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