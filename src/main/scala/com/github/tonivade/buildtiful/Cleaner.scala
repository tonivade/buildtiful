package com.github.tonivade.buildtiful

trait Cleaner {
  def clean(build: Build): Unit
}

class DefaultCleaner extends Cleaner {
  def clean(build: Build) {
    println("clean")
  }
}