package net.sf.mygradlebuild.tasks

import net.sf.mygradlebuild.GradleBuildFileGenerator
import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaLibraryProject

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class ExportGradleBuildFileForJavaLibraryProject extends DefaultTask {

    def String group ='Development'
    def String description= "Creates a template build.gradle for new java library project."

    def parent
    def projectName
    def String[] applyFroms
    def library

    @TaskAction
    def exportBuildFile() {
        def GradleBuildFileGenerator generator =new GradleBuildFileGenerator()
        def GradleProjectBuilderForJavaLibraryProject builder = GradleProjectBuilderForJavaLibraryProject.forProject(projectName).applyFroms(applyFroms).library(library)
        generator.forJavaLibProject(parent, builder)
        printOutInfo("java-development.buildGradleForJavaLibProject", "Template for build.gradle successfully created for ${projectName}. Remember to fix lib and lib-sources to reflect the actual project.")
    }
    void printOutInfo(String taskName, String msg){
        println(msg)
    }
}
