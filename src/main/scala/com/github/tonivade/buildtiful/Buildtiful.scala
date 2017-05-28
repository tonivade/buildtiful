package com.github.tonivade.buildtiful

import cats.Id
import BuilderInterpreter.BuilderInstance

object Buildtiful {
  def main(args: Array[String]) {
    BuilderProgram.build[Id](args(0))
  }
}
