package com.github.tonivade.buildtiful

import io.circe._
import io.circe.generic.auto._
import io.circe.yaml._
import io.circe.yaml.parser
import scala.io.Source
import cats.implicits._

trait Parser {
  def parse(file: String): Build
}

class YamlParser extends Parser {
  def parse(file: String): Build = {
     val yamlString = Source.fromFile(file).mkString
      val json = parser.parse(yamlString)
      println(json)
      // TODO: do not throw exceptions
      json.leftMap(err => err: Error).flatMap(_.as[Build]).valueOr(throw _)
  }
}