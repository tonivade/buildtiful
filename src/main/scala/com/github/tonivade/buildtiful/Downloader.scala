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
  def download(build: Build) {
    println("download")
    val target = new File("target/libs")
    
    val ivySettings = new IvySettings()
    ivySettings.setDefaultCache(new File("ivy/cache"))
    
    val resolver = new IBiblioResolver()
    resolver.setM2compatible(true)
    resolver.setUsepoms(true)
    resolver.setName("central")

    ivySettings.addResolver(resolver)
    ivySettings.setDefaultResolver(resolver.getName())

    val ivy = Ivy.newInstance(ivySettings)
    
    val resolverOptions = new ResolveOptions();
    resolverOptions.setTransitive(true);
    resolverOptions.setDownload(true);

    val moduleDescriptor = DefaultModuleDescriptor.newDefaultInstance(
        ModuleRevisionId.newInstance(
            build.project.groupId, 
            build.project.artifactId + "-envelope", 
            build.project.version
        )                        
    )

    val revisionId = ModuleRevisionId.newInstance(
        build.project.groupId, 
        build.project.artifactId, 
        build.project.version
    )

    val dependencyConfiguration = new DefaultDependencyDescriptor(moduleDescriptor, revisionId, false, false, false)

    dependencyConfiguration.addDependencyConfiguration("default", "master")
    moduleDescriptor.addDependency(dependencyConfiguration)

    val ResolveReport = ivy.resolve(moduleDescriptor, resolverOptions);
    if (ResolveReport.hasError()) {
        throw new RuntimeException(ResolveReport.getAllProblemMessages().toString());
    }

    val module = ResolveReport.getModuleDescriptor()

    ivy.retrieve(
        module.getModuleRevisionId(),
        target.getAbsolutePath() + "/[artifact](-[classifier]).[ext]",
        new RetrieveOptions()
            // this is from the envelop module
            .setConfs(Array[String]("default"))
    )
  }
}