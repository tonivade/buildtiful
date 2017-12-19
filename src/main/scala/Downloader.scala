package com.github.tonivade.buildtiful

import Config._

trait Downloader {
  def download(build: Build) : Unit
}

class IvyDownloader extends Downloader {
  import IvyHelper._
  
  def download(build: Build) {
    println("download")
    
    if (!libs.exists()) libs.mkdirs()

    module(build).map(resolve(_)).map(retrieve(_))
  }
  
 
}