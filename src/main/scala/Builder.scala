package com.github.tonivade.buildtiful

import cats.free.Free
import cats.free.Free.liftF

object BuilderSyntax {
  type Builder[A] = Free[BuildEffect, A]
  
  def read(file: String): Builder[Build] = 
    liftF[BuildEffect, Build](ParseProject(file))

  def clean(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](CleanProject(build))

  def download(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](DownloadDependencies(build))

  def compile(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](CompileProject(build))

  def test(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](RunTests(build))
    
  def makepkg(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](MakePackage(build))

  def deploy(build: Build): Builder[Unit] = 
    liftF[BuildEffect, Unit](DeployPackage(build))
}

import cats.arrow.FunctionK
import cats.{Id, ~>}

object BuilderInterpreter {
  import scala.util.Try

  def builderInterpreter: BuildEffect ~> Try = {
    new (BuildEffect ~> Try) {
        val parser = new YamlParser()
        val downloader = new IvyDownloader()
        val cleaner = new DefaultCleaner()
        val compiler = new DefaultCompiler()
        val tester = new DefaultTester()
        val packager = new DefaultPackager()
        val deployer = new MavenDeployer()

      def apply[A](fa: BuildEffect[A]): Try[A] =
        fa match {
          case ParseProject(file) => Try(parser.parse(file))
          case CleanProject(build) => Try(cleaner.clean(build))
          case DownloadDependencies(build) => Try(downloader.download(build))
          case CompileProject(build) => Try(compiler.compile(build))
          case RunTests(build) => Try(tester.test(build))
          case MakePackage(build) => Try(packager.makepkg(build))
          case DeployPackage(build) => Try(deployer.deploy(build))
        }
    }
  }
}

object BuilderProgram {
  import BuilderSyntax._

  def build(file: String) : Builder[Unit] = 
    for {
      build <- read(file)
      res   <- clean(build)
      res   <- download(build)
      res   <- compile(build)
      res   <- test(build)
      res   <- makepkg(build)
      res   <- deploy(build)
    } yield (res)
  
}
