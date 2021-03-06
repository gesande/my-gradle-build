package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip


class JavaProjectDistribution implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("java")

        project.task('dist', type: Zip) { Zip task ->
            group = 'Distribution'
            description = "Makes the project specific distribution archive."
            task.dependsOn("jar")
            task.from project.jar
            task.from project.configurations.runtime
            task.exclude(project.properties.distributionExcludes)
            doLast { println "Following libraries were excluded when making the $project.name dist: ${project.properties.distributionExcludes} " }
        }

        project.artifacts {
            archives project.dist
            archives project.sourcesJar
        }

        project.task('testCodeDist', type: Zip) { Zip task ->
            group = 'Distribution'
            description = "Makes the project specific test code distribution archive."
            task.dependsOn 'jar'
            task.from project.testSourcesJar
            task.from project.configurations.testRuntime
            task.exclude(project.properties.distributionExcludes)
            doLast { println "Following libraries were excluded when making the $project.name testCodeDist: ${project.properties.distributionExcludes} " }
        }

        project.task('release', dependsOn: ['dist', 'sourcesJar']) {
            group = 'Distribution'
            description = "Defines the project specific release distributions."
        }

        project.task('testCodeRelease', dependsOn: [
            'testCodeDist',
            'testSourcesJar'
        ]) {
            group = 'Distribution'
            description = "Defines the project specific test code release distributions."
        }
    }
}
