package net.sf.mygradlebuild.tasks;

import net.sf.mygradlebuild.GradleBuildFileGenerator
import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaProject

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


public class ExportGradleBuildFileForNewJavaProject extends DefaultTask {

	def String group ='Development'
	def String description ="Creates a template build.gradle for new java project."

	def File parent
	def String projectName
	def String[] applyFroms
	def String[] applyPlugins

	@TaskAction
	def exportBuildFile() {
		def GradleBuildFileGenerator generator =new GradleBuildFileGenerator()
		def GradleProjectBuilderForJavaProject builder = GradleProjectBuilderForJavaProject.forProject(projectName).applyFroms(applyFroms).applyPlugins(applyPlugins)
		generator.forJavaProject(parent, builder)
		println("Build file build.gradle successfully created for project '${projectName}'.")
	}
}
