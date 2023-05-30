package org.example;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "config-create", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DCLSPlugin extends AbstractMojo {
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDirectory", required = true )
    private File outputDirectory;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException {
        String config = createConfigString();
        File f = outputDirectory;
        if ( !f.exists() )
        {
            f.mkdirs();
        }
        File prop = new File( f, "dcls.properties" );
        try (FileWriter w = new FileWriter(prop)) {
            w.write(config);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + prop, e);
        }
    }

    private String createConfigString(){
        StringBuilder config = new StringBuilder();
        config.append("sv=JAVA_")
                .append(project.getProperties().getProperty("maven.compiler.source"))
                .append(":")
                .append("tv=JAVA_")
                .append(project.getProperties().getProperty("maven.compiler.target"))
                .append(":")
                .append("src=")
                .append(project.getCompileSourceRoots().toString().replace("[", "").replace("]", "")).append(":")
                .append("tmp=.");
        if (project.getArtifacts().size() != 0) {
            config.append(":").append("lib=");
            for (final Artifact artifact : project.getArtifacts()) {
                config.append(artifact.getFile().getAbsolutePath()).append(";");
            }
        }
        return config.toString();
    }
}
