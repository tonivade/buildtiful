package com.github.tonivade.buildtiful

import Config._

trait Compiler {
  def compile(build: Build) : Unit
}

class AntCompiler extends Compiler {
  import AntTasks._
  
  def compile(build: Build) {
    println("compile")
    
    if (!classes.exists()) classes.mkdirs()
    
    javac(build).execute()
  }
}