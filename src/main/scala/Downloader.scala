package com.github.tonivade.buildtiful

import Config._

trait Downloader {
  def download(build: Build) : Unit
}

class IvyDownloader extends Downloader {
  import IvyTasks._
  
  def download(build: Build) {
    println("download")
    
    if (!libs.exists()) libs.mkdirs()

    ivyDownload(build).execute()
  }
}