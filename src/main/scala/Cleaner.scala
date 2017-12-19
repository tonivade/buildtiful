package com.github.tonivade.buildtiful

import scala.reflect.io.Path

import Config._

trait Cleaner {
  def clean(build: Build): Unit
}

class DefaultCleaner extends Cleaner {
  def clean(build: Build) {
    println("clean")
    
    Path(target).deleteRecursively()
  }
}