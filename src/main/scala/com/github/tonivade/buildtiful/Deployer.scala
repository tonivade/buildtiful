package com.github.tonivade.buildtiful

trait Deployer {
  def deploy(build: Build): Unit
}

class MavenDeployer extends Deployer {
  def deploy(build: Build) {
    println("deploy")
  }
}