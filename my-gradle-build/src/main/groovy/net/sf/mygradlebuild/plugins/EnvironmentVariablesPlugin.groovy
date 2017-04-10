package net.sf.mygradlebuild.plugins;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class EnvironmentVariablesPlugin implements Plugin<Project> {

	private static final String GROUP = "Environment variables"

	@Override
	public void apply(final Project project) {
		project.task("javaHome") {
			group = GROUP
			description = "Prints out JAVA_HOME."
			doLast { println("${System.getenv('JAVA_HOME')}") }
		}
		project.task("groovyHome") {
			group = GROUP
			description = "Prints out GROOVY_HOME."
			doLast { println("${System.getenv('GROOVY_HOME')}") }
		}
		project.task("gradleHome") {
			group = GROUP
			description = "Prints out GRADLE_HOME."
			doLast { println("${System.getenv('GRADLE_HOME')}") }
		}
		project.task("printOutEnvironment") { Task task ->
			task.dependsOn("javaHome", "groovyHome", "gradleHome")
			group = GROUP
			description = "Prints out environment variables like JAVA_HOME."
		}
	}
}
