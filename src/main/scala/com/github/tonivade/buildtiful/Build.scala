package com.github.tonivade.buildtiful

case class Build(project: Project, sources: Sources, plugins: Seq[String], 
                 repositories: Seq[Repository], dependencies: Dependencies, build: Seq[String])

case class Project(groupId: String, artifactId: String, version: String)

case class Repository(id: String, url: String)

case class Dependencies(compile: Seq[String], test: Seq[String])

case class Sources(main: Seq[String], test: Seq[String])

