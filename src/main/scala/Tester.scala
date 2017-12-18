package com.github.tonivade.buildtiful

import java.io.File

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask.SummaryAttribute
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement.TypeAttribute
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path
import org.apache.tools.ant.Task

import Config._

trait Tester {
  def test(build: Build) : Unit
}

class DefaultTester extends Tester {

  def test(build: Build) {
    println("test")
    
    if (!report.exists()) report.mkdirs()
    
    junit(build).execute()
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
}
