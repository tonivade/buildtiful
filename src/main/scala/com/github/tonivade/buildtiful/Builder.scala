package com.github.tonivade.buildtiful

import io.circe._
import io.circe.generic.auto._
import io.circe.yaml._

case class Build(project: Project, source: Seq[String], plugins: Seq[String], 
                 repositories: Seq[Repository], dependencies: Dependencies, build: Seq[String])

case class Project(groupId: String, artifactId: String, version: String)

case class Repository(id: String, url: String)

case class Dependencies(compile: Seq[String], test: Seq[String])

trait Builder[P[_]] {
  def read(file: String): P[Build]
  def clean(build: Build): P[Unit]
  def download(build: Build): P[Unit]
  def compile(build: Build): P[Unit]
  def test(build: Build): P[Unit]
  def makepkg(build: Build): P[Unit]
  def deploy(build: Build): P[Unit]
}

object Builder {
  def apply[P[_]](implicit Builder: Builder[P]) = Builder

  object Syntax {
    def read[P[_]](file: String)(implicit Builder: Builder[P]) = Builder.read(file)
    def download[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.download(build)
    def clean[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.clean(build)
    def compile[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.compile(build)
    def test[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.test(build)
    def makepkg[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.makepkg(build)
    def deploy[P[_]](build: Build)(implicit Builder: Builder[P]) = Builder.deploy(build)
  }
}

object BuilderInterpreter {
  import cats.{Id, Monad}
  import cats.implicits._
  import io.circe.yaml.parser
  import scala.io.Source

  implicit object BuilderInstance extends Builder[Id] {
    def read(file: String) = {
      val yamlString = Source.fromFile(file).mkString
      val json = parser.parse(yamlString)
      println(json)
      // TODO: do not throw exceptions
      json.leftMap(err => err: Error).flatMap(_.as[Build]).valueOr(throw _)
    }
    def download(build: Build) = println("download")
    def clean(build: Build) = println("clean")
    def compile(build: Build) = println("compile")
    def test(build: Build) = println("test")
    def makepkg(build: Build) = println("makepkg")
    def deploy(build: Build) = println("deploy")
  }
}

object BuilderProgram {
  import cats.Monad
  import cats.implicits._
  import Builder.Syntax._

  def build[P[_]: Builder: Monad](file: String): P[Unit] = {
    for {
      build <- read(file)
      _     <- download(build)
      _     <- clean(build)
      _     <- compile(build)
      _     <- test(build)
      _     <- makepkg(build)
      _     <- deploy(build)
    } yield ()
  }
}