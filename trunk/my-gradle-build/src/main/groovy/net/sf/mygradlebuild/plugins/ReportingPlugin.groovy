package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar

public class ReportingPlugin implements Plugin<Project>{

	@Override
	public void apply(final Project project) {

		project.extensions.create("reportingSettings", ReportingSettingsExtension)

		project.configurations { antClasspath }
		project.dependencies { antClasspath 'org.apache.ant:ant-junit:1.8.2' }

		project.task ("reportingOptions") {
			group = 'Reporting'
			description = "Prints out reporting plugin options"
			doLast {
				println "reportDir = ${project.buildDir}/reports"
				println "toolsDir = ${project.reportingSettings.toolsDirectory}"
				println "findbugsVersion = ${project.reportingSettings.findbugsVersion}"
			}
		}

		project.task ("aggregateTestReport") {
			group = 'Reporting'
			description = "Makes aggregate test report with ant-junit."

			doLast {
				ClassLoader antClassLoader = org.apache.tools.ant.Project.class.classLoader
				project.configurations.antClasspath.each { File f ->
					antClassLoader.addURL(f.toURI().toURL())
				}
				def targetDir = new File("${project.buildDir}/reports", 'junit')
				targetDir.mkdirs()
				def resultsDir = targetDir.getPath()
				println 'Creating test report...'

				ant.taskdef(
						name: 'junitreport',
						classname: 'org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator',
						classpath: project.configurations.antClasspath.asPath
						)

				ant.junitreport(todir: resultsDir) {
					fileset(dir: "${project.projectDir}", includes: '**/build/test-results/**/TEST-*.xml')
					report(todir: targetDir, format: "frames")
				}

				println("Aggregate test report can be found from file://${targetDir}/index.html")
			}
		}

		project.task("aggregateJDependReport") {
			group = 'Reporting'
			description = "Makes aggregate jdepend report with tattletale."
			doLast {
				def targetDir = new File("${project.buildDir}/reports", 'jdepend')
				targetDir.deleteDir()
				targetDir.mkdirs()
				def jarsDir= new File("${project.buildDir}/reports/analyzed-jars")
				jarsDir.deleteDir()
				jarsDir.mkdirs()
				def FileTree jars = project.fileTree("${project.projectDir}").include('**/build/libs/*.jar')
				def FileTree libs = project.fileTree("${project.projectDir}").exclude("${project.reportingSettings.toolsDirectory}").include('**/lib/*.jar')
				project.copy {
					from jars.getFiles() + libs.getFiles()
					into "$jarsDir"
				}
				println 'Creating JDepend report...'
				new ByteArrayOutputStream().withStream { os ->
					def result = project.exec {
						executable = 'java'
						args =[
							'-Xmx1024m',
							'-jar',
							"$project.reportingSettings.toolsDirectory/tattletale-1.1.2.Final/tattletale.jar",
							"${jarsDir}",
							"${targetDir}"
						]
						standardOutput = os
					}
					println("JDepend report can be found from file://${targetDir}/index.html")
				}
			}
		}
		project.task("aggregateFindBugsReport") { Task task ->
			group ='Reporting'
			description = 'Makes aggregate findbugs report.'
			doLast {
				def targetDir = new File("${project.buildDir}/reports", 'findbugs')
				targetDir.deleteDir()
				targetDir.mkdirs()
				def String jreHome = "${System.getenv('JAVA_HOME')}/jre"
				println 'Creating FindBugs report...'
				def List<String> arguments = new ArrayList<String>()
				arguments.add('-javahome')
				arguments.add(jreHome)
				arguments.add('-textui')
				arguments.add('-html')
				arguments.add('-output')
				arguments.add( "${targetDir}/index.html")
				arguments.add('-onlyAnalyze')
				String onlyAnalyzePackages = project.reportingSettings.onlyAnalyze +".-"
				arguments.add(onlyAnalyzePackages)
				arguments.add('-auxclasspath')

				def StringBuilder auxClasspath = new StringBuilder()
				def FileTree libs = project.fileTree("${project.projectDir}").exclude("${project.reportingSettings.toolsDirectory}").include('**/lib/*.jar')
				def FileTree jreLibs = project.fileTree("${jreHome}/lib/").include('*.jar')
				def FileTree extLibs = project.fileTree("${project.reportingSettings.toolsDirectory}/ext-libs-for-findbugs/").include('*.jar')
				for(File libJar : libs){
					auxClasspath.append(libJar.getPath())
					auxClasspath.append(File.pathSeparator)
				}
				for(File jar : jreLibs) {
					auxClasspath.append(jar.getPath())
					auxClasspath.append(File.pathSeparator)
				}
				for(File jar : extLibs) {
					auxClasspath.append(jar.getPath())
					auxClasspath.append(File.pathSeparator)
				}
				arguments.add(auxClasspath.toString())

				def FileTree jars = project.fileTree("${project.projectDir}").include('**/build/libs/*.jar')
				for(File jarFile : jars ){
					arguments.add(jarFile.getPath())
				}

				new ByteArrayOutputStream().withStream { os ->
					def result = project.exec {
						executable = "${project.reportingSettings.toolsDirectory}/findbugs-${project.reportingSettings.findbugsVersion}/bin/findbugs"
						args = arguments
						standardOutput = os
					}
				}
				println("Findbugs report can be found from file://${targetDir}/index.html")
			}
		}
		project.gradle.projectsEvaluated { addTasksAfterProjectsEvaluated(project) }
	}

	void addTasksAfterProjectsEvaluated(final Project project){
		project.task("archiveAggregateReports", type: Tar)  { Tar task ->
			group = 'Archive'
			description = 'Archive aggregate reports including junit tests/pmd/findbugs/jdepend'
			def timestamp = new Date(System.currentTimeMillis()).format("yyyyMMdd-HHmmss")
			from ("${project.buildDir}/reports")
			// Set destination directory.
			def File parent = project.file("${project.buildDir}/reports").getParentFile()
			task.destinationDir = parent
			// Set filename properties.
			task.baseName = "report-artifacts-${timestamp}"
			extension = 'tar.gz'
			//task.version = "${project.properties.artifactVersion}"
			compression = Compression.GZIP
			doLast {
				def String tarFile = "${parent}/${task.archiveName}"
				println("Report artifact archive can be found from file://$tarFile")
			}
		}

	}

}

class ReportingSettingsExtension {
	String toolsDirectory
	String onlyAnalyze
	String findbugsVersion
}

