package net.sf.mygradlebuild.plugins

import net.sf.mygradlebuild.tasks.BashExec

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

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
                    def outputFactory = services.get(StyledTextOutputFactory).create("SvnPlugin.svnRevision")
                    outputFactory.withStyle(Style.Info).println("${matchLastChangedRev[0][1]}")
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
                def outputFactory = services.get(StyledTextOutputFactory).create("SvnPlugin.svnStatus")
                outputFactory.withStyle(Style.Info).println(svnStatus.isEmpty() ? "No changes" : "${svnStatus}")
                outputFactory.withStyle(Style.Info).println("Revision: ${svnversion}")
            }
        }
        project.task("listFilesNotAddedToSvn", type : BashExec, { BashExec task ->
            task.group ='Svn'
            task.description ='List of files which are not added to svn yet.'
            task.args  "for i in \$(svn st | grep ? | sed 's/?//'); do echo \$(realpath \$i); done;"
        })

        project.task("addNewFilesToSvn", type : BashExec, { BashExec task ->
            task.group ='Svn'
            task.description ='Add the new files into svn.'
            task.args  "for i in \$(svn st | grep ? | sed 's/?//'); do svn add \$i; done;"
        })
    }
}

