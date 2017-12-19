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

import Config._

object IvyHelper {
  
  val ivy: Ivy = {
    val ivySettings = new IvySettings()
    ivySettings.setDefaultCache(cache)
    
    val resolver = new IBiblioResolver()
    resolver.setM2compatible(true)
    resolver.setUsepoms(true)
    resolver.setName("central")

    ivySettings.addResolver(resolver)
    ivySettings.setDefaultResolver(resolver.getName())

    Ivy.newInstance(ivySettings)
  }
  
  def module(build: Build) : Option[ModuleDescriptor] = {
    val module = DefaultModuleDescriptor.newDefaultInstance(
        ModuleRevisionId.newInstance(
            build.project.groupId, 
            build.project.artifactId, 
            build.project.version
        )                        
    )
    
    val dependencies = 
      createDependencies(module, build.dependencies.compile) ++ 
      createDependencies(module, build.dependencies.test)
    
    for {
      dep <- dependencies
    } yield module.addDependency(dep)
    
    Some(module)
  }
  
  def createDependencies(module: ModuleDescriptor, deps: Seq[String]) : Seq[DependencyDescriptor] = {
    deps.map(dep => {
      val dependency = dep.split(":")
      val revisionId = ModuleRevisionId.newInstance(dependency(0), dependency(1), dependency(2))

      val dependencyDescriptor = new DefaultDependencyDescriptor(module, revisionId, false, false, false)

      dependencyDescriptor.addDependencyConfiguration("default", "master")
      
      dependencyDescriptor
    })
  }
  
  def resolve(module: ModuleDescriptor) : ResolveReport = {
    val options = new ResolveOptions()
    options.setTransitive(true)
    options.setDownload(true)
    val report = ivy.resolve(module, options)
    if (report.hasError()) {
      // TODO: do not throw exception
      throw new RuntimeException(report.getAllProblemMessages().toString())
    }
    report
  }
  
  def retrieve(resolveReport: ResolveReport) {
    val module = resolveReport.getModuleDescriptor()
    val options = new RetrieveOptions().setConfs(Array[String]("default"))
    
    ivy.retrieve(
        module.getModuleRevisionId(),
        libs.getAbsolutePath() + "/[artifact](-[classifier]).[ext]",
        options)
  } 
}