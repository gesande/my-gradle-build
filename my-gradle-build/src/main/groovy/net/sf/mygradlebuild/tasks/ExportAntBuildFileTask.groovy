package net.sf.mygradlebuild.tasks;

import net.sf.mygradlebuild.AntBuildFileGeneratorForGradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction


public class ExportAntBuildFileTask extends DefaultTask {
    def parent
    def buildFilename="build.xml"
    def defaultTarget=""
    def String[] targets

    File getParent() {
        project.file(parent)
    }

    @TaskAction
    def exportBuildFile() {
        if(targets==null || targets.length ==0) {
            throw new GradleException("Cannot export ant build file! No targets were defined!");
        }
        AntBuildFileGeneratorForGradle generator = new AntBuildFileGeneratorForGradle()
        generator.generate(getParent(),buildFilename, defaultTarget, targets)
    }
}
