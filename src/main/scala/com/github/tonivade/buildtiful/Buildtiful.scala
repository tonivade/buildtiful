package com.github.tonivade.buildtiful

import BuilderInterpreter._
import BuilderProgram._

object Buildtiful {
  def main(args: Array[String]) {
    args.headOption match {
      case Some(file) => build(file)
      case _ => println("invalid arguments")
    }
  }
}
