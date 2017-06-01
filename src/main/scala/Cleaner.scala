package com.github.tonivade.buildtiful

import java.io.File

trait Cleaner {
  def clean(build: Build): Unit
}

class DefaultCleaner extends Cleaner {
  def clean(build: Build) {
    println("clean")
    
    val target = new File("example/target")
    
    if (target.exists()) target.delete();
  }
}