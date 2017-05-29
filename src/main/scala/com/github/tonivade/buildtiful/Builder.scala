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
  import cats.{Id, Monad}
  import cats.implicits._
  
  val parser = new YamlParser()
  val downloader = new IvyDownloader()
  val cleaner = new DefaultCleaner()
  val compiler = new DefaultCompiler()
  val tester = new DefaultTester()
  val packager = new DefaultPackager()
  val deployer = new MavenDeployer()

  implicit object BuilderInstance extends Builder[Id] {
    def read(file: String) = parser.parse(file)
    def download(build: Build) = downloader.download(build)
    def clean(build: Build) = cleaner.clean(build)
    def compile(build: Build) = compiler.compile(build)
    def test(build: Build) = tester.test(build)
    def makepkg(build: Build) = packager.makepkg(build)
    def deploy(build: Build) = deployer.deploy(build)
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