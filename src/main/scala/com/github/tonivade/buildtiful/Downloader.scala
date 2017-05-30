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

    val moduleDescriptor = DefaultModuleDescriptor.newDefaultInstance(
        ModuleRevisionId.newInstance(
            build.project.groupId, 
            build.project.artifactId, 
            build.project.version
        )                        
    )

    val dependencies = build.dependencies.compile.map(dep => {
      val dependency = dep.split(":")
      val revisionId = ModuleRevisionId.newInstance(dependency(0), dependency(1), dependency(2))

      val dependencyDescriptor = new DefaultDependencyDescriptor(moduleDescriptor, revisionId, false, false, false)

      dependencyDescriptor.addDependencyConfiguration("default", "master")
      
      dependencyDescriptor
    })
    
    for {
      dep <- dependencies
    } yield moduleDescriptor.addDependency(dep)

    val resolverOptions = new ResolveOptions();
    resolverOptions.setTransitive(true);
    resolverOptions.setDownload(true);
    val ResolveReport = ivy.resolve(moduleDescriptor, resolverOptions);
    if (ResolveReport.hasError()) {
        throw new RuntimeException(ResolveReport.getAllProblemMessages().toString());
    }

    val module = ResolveReport.getModuleDescriptor()
    val retrieverOptions = new RetrieveOptions().setConfs(Array[String]("default"))
    val target = new File("target/libs")

    ivy.retrieve(
        module.getModuleRevisionId(),
        target.getAbsolutePath() + "/[artifact](-[classifier]).[ext]",
        retrieverOptions)
  }
}