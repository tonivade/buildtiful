package com.github.tonivade.buildtiful

import org.apache.ivy.core.settings.IvySettings
import java.io.File
import org.apache.ivy.Ivy
import org.apache.ivy.plugins.resolver.IBiblioResolver
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.retrieve.RetrieveOptions
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.descriptor.DependencyDescriptor

trait Downloader {
  def download(build: Build) : Unit
}

class IvyDownloader extends Downloader {

  val ivy: Ivy = {
    val ivySettings = new IvySettings()
    ivySettings.setDefaultCache(new File("ivy/cache"))
    
    val resolver = new IBiblioResolver()
    resolver.setM2compatible(true)
    resolver.setUsepoms(true)
    resolver.setName("central")

    ivySettings.addResolver(resolver)
    ivySettings.setDefaultResolver(resolver.getName())

    Ivy.newInstance(ivySettings)
  }
  
  def download(build: Build) {
    println("download")

    createModuleDescriptor(build).map(resolve(_)).map(retrieve(_))
  }
  
  def createModuleDescriptor(build: Build) : Option[ModuleDescriptor] = {
    val moduleDescriptor = DefaultModuleDescriptor.newDefaultInstance(
        ModuleRevisionId.newInstance(
            build.project.groupId, 
            build.project.artifactId, 
            build.project.version
        )                        
    )
    
    val dependencies = 
      createDependencies(moduleDescriptor, build.dependencies.compile) ++ 
      createDependencies(moduleDescriptor, build.dependencies.test)
    
    for {
      dep <- dependencies
    } yield moduleDescriptor.addDependency(dep)
    
    Some(moduleDescriptor)
  }
  
  def createDependencies(moduleDescriptor: ModuleDescriptor, deps: Seq[String]) : Seq[DependencyDescriptor] = {
    deps.map(dep => {
      val dependency = dep.split(":")
      val revisionId = ModuleRevisionId.newInstance(dependency(0), dependency(1), dependency(2))

      val dependencyDescriptor = new DefaultDependencyDescriptor(moduleDescriptor, revisionId, false, false, false)

      dependencyDescriptor.addDependencyConfiguration("default", "master")
      
      dependencyDescriptor
    })
  }
  
  def resolve(moduleDescriptor: ModuleDescriptor) : ResolveReport = {
    val resolverOptions = new ResolveOptions()
    resolverOptions.setTransitive(true)
    resolverOptions.setDownload(true)
    val resolveReport = ivy.resolve(moduleDescriptor, resolverOptions)
    if (resolveReport.hasError()) {
        throw new RuntimeException(resolveReport.getAllProblemMessages().toString())
    }
    resolveReport
  }
  
  def retrieve(resolveReport: ResolveReport) {
    val module = resolveReport.getModuleDescriptor()
    val retrieverOptions = new RetrieveOptions().setConfs(Array[String]("default"))
    val target = new File("target/libs")
    
    ivy.retrieve(
        module.getModuleRevisionId(),
        target.getAbsolutePath() + "/[artifact](-[classifier]).[ext]",
        retrieverOptions)
  }
}