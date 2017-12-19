package com.github.tonivade.buildtiful

import Config._

trait Compiler {
  def compile(build: Build) : Unit
}

class AntCompiler extends Compiler {
  import AntHelper._
  
  def compile(build: Build) {
    println("compile")
    
    if (!classes.exists()) classes.mkdirs()
    
    javac(build).execute()
  }
}