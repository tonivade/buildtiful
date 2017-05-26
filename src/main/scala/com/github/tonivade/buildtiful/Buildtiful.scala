package com.github.tonivade.buildtiful

import io.circe._
import io.circe.generic.auto._
import io.circe.yaml._
import scala.io.Source

case class Build (project: Project, plugins: Seq[String], repositories: Seq[Repository], dependencies: Dependencies, build: Seq[String])

case class Project (groupId: String, artifactId: String, version: String)

case class Repository (id: String, url: String)

case class Dependencies (compile: Seq[String], test: Seq[String])

object Buildtiful {
  def main(args: Array[String]) {
    import io.circe.yaml.parser
    
    val yamlString = Source.fromFile(args(0)).mkString
    
    val build = parser.parse(yamlString).fold(x => x, _.as[Build])
    
    println(build)
  }
}


