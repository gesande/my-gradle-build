package net.sf.mygradlebuild.plugins;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style


public class EnvironmentVariablesPlugin implements Plugin<Project> {

    private static final String GROUP = "Environment variables"

    @Override
    public void apply(final Project project) {
        project.task("javaHome") {
            group = GROUP
            description = "Prints out JAVA_HOME."
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("environment.javaHome")
                outputFactory.withStyle(Style.Info).println("${System.getenv('JAVA_HOME')}")
            }
        }
        project.task("groovyHome") {
            group = GROUP
            description = "Prints out GROOVY_HOME."
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("environment.groovyHome")
                outputFactory.withStyle(Style.Info).println("${System.getenv('GROOVY_HOME')}")
            }
        }
        project.task("gradleHome") {
            group = GROUP
            description = "Prints out GRADLE_HOME."
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("environment.gradleHome")
                outputFactory.withStyle(Style.Info).println("${System.getenv('GRADLE_HOME')}")
            }
        }
        project.task("printOutEnvironment") { Task task ->
            task.dependsOn("javaHome", "groovyHome", "gradleHome")
            group = GROUP
            description = "Prints out environment variables like JAVA_HOME."
        }
    }
}
