package com.github.tonivade.buildtiful

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
    def read[P[_]](file: String)(implicit B: Builder[P]) = B.read(file)
    def download[P[_]](build: Build)(implicit B: Builder[P]) = B.download(build)
    def clean[P[_]](build: Build)(implicit B: Builder[P]) = B.clean(build)
    def compile[P[_]](build: Build)(implicit B: Builder[P]) = B.compile(build)
    def test[P[_]](build: Build)(implicit B: Builder[P]) = B.test(build)
    def makepkg[P[_]](build: Build)(implicit B: Builder[P]) = B.makepkg(build)
    def deploy[P[_]](build: Build)(implicit B: Builder[P]) = B.deploy(build)
  }
}

object BuilderInterpreter {
  import cats.Monad
  import scala.util.Try
  import scala.util.Failure
  import scala.util.Success
  
  val parser = new YamlParser()
  val downloader = new IvyDownloader()
  val cleaner = new DefaultCleaner()
  val compiler = new DefaultCompiler()
  val tester = new DefaultTester()
  val packager = new DefaultPackager()
  val deployer = new MavenDeployer()
  
  implicit object BuilderInstance extends Builder[Try] {
    def read(file: String) = Try(parser.parse(file))
    def download(build: Build) = Try(downloader.download(build))
    def clean(build: Build) = Try(cleaner.clean(build))
    def compile(build: Build) = Try(compiler.compile(build))
    def test(build: Build) = Try(tester.test(build))
    def makepkg(build: Build) = Try(packager.makepkg(build))
    def deploy(build: Build) = Try(deployer.deploy(build))
  }
}

object BuilderProgram {
  import Builder.Syntax._
  import cats.Monad
  import cats.syntax.functor._
  import cats.syntax.flatMap._

  def build[P[_]: Builder: Monad](file: String): P[Unit] = {
    for {
      build <- read(file)
      _     <- clean(build)
      _     <- download(build)
      _     <- compile(build)
      _     <- test(build)
      _     <- makepkg(build)
      _     <- deploy(build)
    } yield ()
  }
}