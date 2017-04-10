package net.sf.mygradlebuild.plugins;

import net.sf.mygradlebuild.tasks.ExportGradleBuildFileForJavaLibraryProject
import net.sf.mygradlebuild.tasks.ExportGradleBuildFileForNewJavaProject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class NewJavaSubProjectUsingSvnPlugin implements Plugin<Project> {

	@Override
	public void apply(final Project project) {
		project.subprojects { apply plugin: NewJavaSubProject  }

		project.task("addProjectToSvn") { Task task ->
			group 'Svn'
			description 'Add project to SVN.'
			task.doLast {
				def out= ""
				new ByteArrayOutputStream().withStream { os ->
					def result = project.parent.exec {
						executable = 'svn'
						args = [
							'add',
							"${project.name}"
						]
						standardOutput = os
					}
					out = os.toString()
				}
				println("Project ${project.name} was added to SVN")
			}
		}

		project.task("generateSvnIgnoreFile") { Task task ->
			group 'Svn'
			description "Generates SVN ignore file."
			task.doLast {
				def StringBuilder sb = new StringBuilder()
				sb.append("build\n")
				sb.append("target\n")
				sb.append(".project\n")
				sb.append(".classpath\n")
				sb.append(".settings\n")
				sb.append(".pmd\n")
				sb.append(".gradle\n")
				def File svnIgnore = project.file("svn-ignore")
				net.sf.mygradlebuild.FileWriter writer = new net.sf.mygradlebuild.FileWriter()
				writer.writeToFile(svnIgnore, sb.toString())
				println("The svn-ignore file was created for project ${project.name}.")
			}
		}

		project.task("applySvnIgnoreFromGeneratedFile") { Task task ->
			task.dependsOn("generateSvnIgnoreFile")
			group 'Svn'
			description "Applies SVN ignore based on contents in project's svn-ignore file."
			task.doLast {
				def out= ""
				new ByteArrayOutputStream().withStream { os ->
					def result =project.exec {
						executable = 'svn'
						args = [
							'propset',
							'svn:ignore',
							'-F',
							"${task.project.projectDir}/svn-ignore",
							"."
						]
						standardOutput = os
					}
					out = os.toString()
				}
				println("Svn ignore applied as defined in ${task.project.projectDir}/svn-ignore in project ${task.project.name}.")
			}
		}
	}
}
