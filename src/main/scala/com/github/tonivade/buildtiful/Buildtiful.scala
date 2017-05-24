package com.github.tonivade.buildtiful

import net.jcazevedo.moultingyaml._
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._

object Buildtiful {
  def main(args: Array[String]) {
  }
}

case class Build (project: Project, plugins: Seq[String], repositories: Seq[Repository], depencencies: Dependencies, build: Seq[String])

case class Project (groupId: String, artifactId: String, version: String)

case class Repository (id: String, url: String)

case class Dependencies (compile: Seq[String], test: Seq[String])

