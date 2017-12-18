package com.github.tonivade.buildtiful

import java.io.File

import Config._

trait Cleaner {
  def clean(build: Build): Unit
}

class DefaultCleaner extends Cleaner {
  def clean(build: Build) {
    println("clean")
    
    if (target.exists()) target.delete()
  }
}