package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd

class ForkPmdSettings implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("pmd");

        project.tasks.withType(Pmd) {
            doFirst {
                project.copy {
                    from project.file("${project.properties.pmdSettings}")
                    into project.projectDir
                }
                println "Pmd template file copied for project '${project.name}'"
            }
        }
        project.apply {
            project.pmd.ruleSetFiles = project.files(".pmd")
            project.pmd.ignoreFailures = 'false'
        }
    }
}
