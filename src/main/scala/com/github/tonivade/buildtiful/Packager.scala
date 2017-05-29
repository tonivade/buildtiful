package com.github.tonivade.buildtiful

trait Packager {
  def makepkg(build: Build): Unit
}

class DefaultPackager extends Packager {
  def makepkg(build: Build) {
    println("makepkg")
  }
}