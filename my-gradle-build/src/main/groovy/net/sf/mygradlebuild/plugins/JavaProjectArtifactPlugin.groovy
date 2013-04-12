package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

class JavaProjectArtifactPlugin implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("java");

        project.task("sourcesJar", type: Jar, dependsOn:"classes") {
            group = 'Distribution'
            description = "Makes the project specific sourceJar."
            classifier = 'sources'
            from project.sourceSets.main.allSource
        }

        project.task("testSourcesJar", type: Jar, dependsOn:"classes") {
            group = 'Distribution'
            description = "Makes the project specific testSourceJar."
            classifier = 'tests'
            from project.sourceSets.test.allSource
        }

        project.task("javadocJar", type: Jar, dependsOn: "javadoc") {
            group = 'Distribution'
            description = "Makes the project specific javadoc jar."
            classifier = 'javadoc'
            from project.javadoc.destinationDir
        }
    }
}
