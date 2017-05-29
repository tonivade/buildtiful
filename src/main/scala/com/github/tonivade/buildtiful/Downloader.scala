package com.github.tonivade.buildtiful

trait Downloader {
  def download(build: Build) : Unit
}

class IvyDownloader extends Downloader {
  def download(build: Build) {
    println("download")
  }
}