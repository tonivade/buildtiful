package com.github.tonivade.buildtiful

import java.io.File

object Config {
  private val _baseDir: String = "example"
  
  val baseDir = new File(_baseDir)

  
  def path(file: String) : String = _baseDir + file
  def file(file: String) : File = new File(path(file))

  val target: File = file("/target")
  val libs: File = file("/target/libs")
  val cache: File = file("/ivy/cache")
  val classes: File = file("/target/classes")
  val report: File = file("/target/report")
}