package org.example;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.stream.Collectors;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @phase process-sources
 */
@Mojo(name = "config-create", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class MyMojo
    extends AbstractMojo
{
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException {
        StringBuilder config = new StringBuilder();
        config.append("sv=")
                .append(project.getProperties().getProperty("maven.compiler.source"))
                .append("tv=")
                .append(project.getProperties().getProperty("maven.compiler.target"))
                .append("src=")
                .append(project.getCompileSourceRoots())
                        .append("tmp=.")
                                .append("lib=");
        for(Dependency dependency: project.getDependencies()){
            config.append(dependency.getSystemPath()).append(";");
        }
        System.out.println(config);
    }
}
