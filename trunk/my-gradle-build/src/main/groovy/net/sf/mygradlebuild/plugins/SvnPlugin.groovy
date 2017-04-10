package net.sf.mygradlebuild.plugins


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import groovyjarjarcommonscli.CommandLine

public final class SvnPlugin implements Plugin<Project>{

	private static final String TASK_GROUP = "Svn"

	@Override
	public void apply(final Project project) {

		project.task("svnRevision") { Task task ->
			task.description='Shows svn revision grepped from svn info.'
			task.group=TASK_GROUP

			doLast {
				new ByteArrayOutputStream().withStream { os ->
					def result =  project.exec {
						executable = 'svn'
						args = ['info']
						standardOutput = os
					}
					def outputAsString = os.toString()
					def matchLastChangedRev = outputAsString =~ /Last Changed Rev: (\d+)/
					println("${matchLastChangedRev[0][1]}")
				}
			}
		}

		project.task("svnStatus") { Task task ->
			task.description ='Shows svn st and svnversion nicely outputted.'
			task.group = TASK_GROUP
			doLast {
				def svnStatus=""
				new ByteArrayOutputStream().withStream { os ->
					def result = project.exec {
						executable = 'svn'
						args = ['st']
						standardOutput = os
					}
					svnStatus = os.toString()
				}
				def svnversion=""
				new ByteArrayOutputStream().withStream { os ->
					def result = project.exec {
						executable = 'svnversion'
						standardOutput = os
					}
					svnversion = os.toString()
				}
				println(svnStatus.isEmpty() ? "No changes" : "${svnStatus}")
				println("Revision: ${svnversion}")
			}
		}
		project.task("listFilesNotAddedToSvn",{ task ->
			task.group ='Svn'
			task.description ='List of files which are not added to svn yet.'
			commandLine "for i in \$(svn st | grep ? | sed 's/?//'); do echo \$(realpath \$i); done;"
		})

		project.task("addNewFilesToSvn", type : { task ->
			task.group ='Svn'
			task.description ='Add the new files into svn.'
			commandLine "for i in \$(svn st | grep ? | sed 's/?//'); do svn add \$i; done;"
		})
	}
}

