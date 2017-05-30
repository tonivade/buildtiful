package com.github.tonivade.buildtiful

trait Tester {
  def test(build: Build) : Unit
}

class DefaultTester extends Tester {
  def test(build: Build) {
    println("test")
  }
}