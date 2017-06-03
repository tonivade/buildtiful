package com.github.tonivade.buildtiful

import BuilderInterpreter._
import BuilderProgram._
import cats.instances.try_._
import scala.util.Try

object Buildtiful {
  def main(args: Array[String]) {
    args.headOption match {
      case Some(file) => println(build(file))
      case _ => println("invalid arguments")
    }
  }
}
