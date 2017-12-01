package com.github.tonivade.buildtiful

import java.io.File

import org.apache.tools.ant.{ Project => AntProject }
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask.SummaryAttribute
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement.TypeAttribute

trait Tester {
  def test(build: Build) : Unit
}

class DefaultTester extends Tester {
  val project : AntProject = {
    val project = new AntProject()
    project.setBaseDir(new File(Config.baseDir))
    project.init() 
    project
  }
  
  def test(build: Build) {
    println("test")
    
    val junit = new JUnitTask()
    val printsummary = new SummaryAttribute();
    printsummary.setValue("true")
    junit.setPrintsummary(printsummary)
    junit.setProject(project)
    
    val classpath = junit.createClasspath();
    
    val fileset = new FileSet()
    fileset.setDir(new File(Config.baseDir + "/libs"))
    fileset.setIncludes("*.jar")
    classpath.addFileset(fileset)
    classpath.add(new Path(project, Config.baseDir + "/target/classes"))
    
    val tests = junit.createBatchTest()
    tests.setFork(true)
    tests.setTodir(new File(Config.baseDir + "/target/report"))
    val testsFileSet = new FileSet()
    testsFileSet.setDir(new File(Config.baseDir + "/target/classes"))
    testsFileSet.setIncludes("**/*Test.class")
    tests.addFileSet(testsFileSet)
    
    val formatter = new FormatterElement()
    val formatterType = new TypeAttribute()
    formatterType.setValue("plain")
    formatter.setType(formatterType)
    junit.addFormatter(formatter)
    
    junit.execute()
  }
}
