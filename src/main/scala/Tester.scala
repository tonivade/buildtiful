package com.github.tonivade.buildtiful

import Config._

trait Tester {
  def test(build: Build) : Unit
}

class AntTester extends Tester {
  import AntTasks._
  
  def test(build: Build) {
    println("test")
    
    if (!report.exists()) report.mkdirs()
    
    junit(build).execute()
  }
}
