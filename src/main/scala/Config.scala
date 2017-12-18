package com.github.tonivade.buildtiful

import java.io.File
import org.apache.tools.ant.{ Project => AntProject }

object Config {
  private val _baseDir: String = "example"
  
  val baseDir = new File(_baseDir)
  
  val project : AntProject = {
    val project = new AntProject()
    project.setBaseDir(baseDir)
    project.init() 
    project
  }
  
  def path(file: String) : String = _baseDir + file
  def file(file: String) : File = new File(path(file))

  val target: File = file("/target")
  val libs: File = file("/target/libs")
  val ivycache: File = file("/target/ivy/cache")
  val classes: File = file("/target/classes")
  val report: File = file("/target/report")
}