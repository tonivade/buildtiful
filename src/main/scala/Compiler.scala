package com.github.tonivade.buildtiful

trait Compiler {
  def compile(build: Build) : Unit
}

class DefaultCompiler extends Compiler {
  def compile(build: Build) {
    println("compile")
  }
}