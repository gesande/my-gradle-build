package net.sf.mygradlebuild;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaLibraryProject;
import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaProject;

import org.junit.Test;

public class GradleBuildFileGeneratorTest {

    @SuppressWarnings("static-method")
    @Test
    public void forJavaProject() throws IOException {
        final GradleBuildFileGenerator generator = new GradleBuildFileGenerator();
        final String project = "javaproject";
        final File parent = new File("target", project);
        GradleProjectBuilderForJavaProject builder = GradleProjectBuilderForJavaProject
                .forProject(project)
                .applyFroms("\"$emmaPlugin\"")
                .applyPlugins(
                        "net.sf.mygradlebuild.plugins.JavaProjectDistribution");
        generator.forJavaProject(parent, builder);
        assertEquals(
                "project(':javaproject') {\n    apply from : \"$emmaPlugin\"\n    apply plugin : net.sf.mygradlebuild.plugins.JavaProjectDistribution\n}\n",
                readBuildFile(parent));
    }

    @SuppressWarnings("static-method")
    @Test
    public void forJavaLibProject() throws IOException {
        final GradleBuildFileGenerator generator = new GradleBuildFileGenerator();
        final String project = "lib-1.0.0";
        final File parent = new File("target", project);
        GradleProjectBuilderForJavaLibraryProject builder = GradleProjectBuilderForJavaLibraryProject
                .forProject(project).applyFroms("\"$libraryPlugin\"")
                .library(project);
        generator.forJavaLibProject(parent, builder);
        assertEquals(
                "project(':lib-1.0.0') { prj ->\n    apply from : \"$libraryPlugin\"\n    prj.ext.library = 'lib-1.0.0.jar'\n    prj.ext.librarySources = 'lib-1.0.0-sources.jar'\n}\n",
                readBuildFile(parent));
    }

    private static String readBuildFile(final File parent)
            throws FileNotFoundException, IOException {
        return ReadFileToStringBuilder.readBuildFile(parent, "build.gradle")
                .toString();
    }
}
