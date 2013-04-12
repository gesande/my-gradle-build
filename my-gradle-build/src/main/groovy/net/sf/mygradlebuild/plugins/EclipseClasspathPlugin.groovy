package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

class EclipseClasspathPlugin implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("eclipse")

        project.apply {
            project.eclipse.classpath.defaultOutputDir = project.file('target/classes')
        }

        project.task("removeTarget") { Task task ->
            group = 'Eclipse'
            description = "Deletes project specific eclipse target -directory."
            doLast {
                project.delete('target')
                services.get(StyledTextOutputFactory).create("removeTarget").withStyle(Style.Info).println( "Target -directory deleted from project ${task.project.name}.")
            }
        }

        project.task("removeEclipseClasses") { Task task ->
            group = 'Eclipse'
            description = "Deletes project specific eclipse target/classes -directory."
            doLast {
                project.delete('target/classes')
                services.get(StyledTextOutputFactory).create("removeEclipseClasses").withStyle(Style.Info).println( "Classes -directory deleted from project ${task.project.name}/target.")
            }
        }
    }
}
