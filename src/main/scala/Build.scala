package com.github.tonivade.buildtiful

case class Build(project: Project, sources: Sources, plugins: Seq[String], 
                 repositories: Seq[Repository], dependencies: Dependencies, build: Seq[String])
case class Project(groupId: String, artifactId: String, version: String)
case class Repository(id: String, url: String)
case class Dependencies(compile: Seq[String], test: Seq[String])
case class Sources(main: Seq[String], test: Seq[String])

sealed abstract class BuildError extends Throwable
case class ParseError(error: String) extends BuildError
case class DownloadError(error: String) extends BuildError
case class CompilationError(error: String) extends BuildError
case class TestError(error: String) extends BuildError
case class PackageError(error: String) extends BuildError
case class DeployError(error: String) extends BuildError

sealed trait BuildEffect[A]
case class ParseProject(file: String) extends BuildEffect[Build]
case class CleanProject(build: Build) extends BuildEffect[Unit]
case class DownloadDependencies(build: Build) extends BuildEffect[Unit]
case class CompileProject(build: Build) extends BuildEffect[Unit]
case class RunTests(build: Build) extends BuildEffect[Unit]
case class MakePackage(build: Build) extends BuildEffect[Unit]
case class DeployPackage(build: Build) extends BuildEffect[Unit]

trait Task {
  def execute()
}


