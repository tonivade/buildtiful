package com.github.tonivade.buildtiful

import org.apache.tools.ant.{Project => AntProject}
import java.io.File
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask
import org.apache.tools.ant.taskdefs.optional.junit.BatchTest
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask.SummaryAttribute

trait Tester {
  def test(build: Build) : Unit
}

class DefaultTester extends Tester {
  val project : AntProject = {
    val project = new AntProject()
    project.setBaseDir(new File("example"))
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
    fileset.setDir(new File("example/libs"))
    fileset.setIncludes("*.jar")
    classpath.addFileset(fileset)
    
    val tests = junit.createBatchTest()
    tests.setFork(true)
    val testsFileSet = new FileSet()
    testsFileSet.setDir(new File("example/target/classes"))
    testsFileSet.setIncludes("*Test.class")
    tests.addFileSet(testsFileSet)
    
    junit.execute()
  }
}