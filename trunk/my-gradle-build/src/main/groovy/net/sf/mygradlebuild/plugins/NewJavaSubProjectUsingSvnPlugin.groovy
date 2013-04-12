package net.sf.mygradlebuild.plugins;

import net.sf.mygradlebuild.tasks.ExportGradleBuildFileForJavaLibraryProject
import net.sf.mygradlebuild.tasks.ExportGradleBuildFileForNewJavaProject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style


public class NewJavaSubProjectUsingSvnPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {

        project.task("createLibDirs") { Task task ->
            group 'Development'
            description 'Creates lib and lib-sources -directories under the given project.'
            task.doLast {
                def libDir = new File(task.project.projectDir, 'lib')
                libDir.mkdirs()
                def libSourcesDir = new File(task.project.projectDir, 'lib-sources')
                libSourcesDir.mkdirs()
            }
        }
        project.task("createJavaDirs") { Task task ->
            group 'Development'
            description 'Create directory structures for a Java project.'
            task.doLast {
                project.sourceSets*.java.srcDirs*.each { it.mkdirs() }
                project.sourceSets*.resources.srcDirs*.each { it.mkdirs() }
            }
        }
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
                services.get(StyledTextOutputFactory).create("java-development.addProjectToSvn").withStyle(Style.Info).println("Project ${project.name} was added to SVN")
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
                services.get(StyledTextOutputFactory).create("java-development.generateSvnIgnoreFile").withStyle(Style.Info).println("The svn-ignore file was created for project ${project.name}.")
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
                services.get(StyledTextOutputFactory).create("java-development.applySvnIgnore").withStyle(Style.Info).println("Svn ignore applied as defined in ${task.project.projectDir}/svn-ignore in project ${task.project.name}.")
            }
        }

        project.task("buildGradleForJavaProject",type: ExportGradleBuildFileForNewJavaProject) { ExportGradleBuildFileForNewJavaProject task ->
            task.parent = task.project.projectDir
            task.projectName = task.project.name
            task.applyFroms = ["\"\$emmaPlugin\""]
            task.applyPlugins = [
                "net.sf.mygradlebuild.plugins.JavaProjectDistribution"
            ]
        }

        project.task("buildGradleForJavaLibProject",type: ExportGradleBuildFileForJavaLibraryProject) { ExportGradleBuildFileForJavaLibraryProject task ->
            task.parent = task.project.projectDir
            task.projectName = task.project.name
            task.applyFroms = [
                "net.sf.mygradlebuild.plugins.JavaLibraryProject"
            ]
            task.library = task.project.name
        }
    }
}
