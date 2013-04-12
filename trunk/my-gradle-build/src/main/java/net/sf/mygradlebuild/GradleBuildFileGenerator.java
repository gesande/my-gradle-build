package net.sf.mygradlebuild;

import java.io.File;
import java.io.IOException;

import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaLibraryProject;
import net.sf.mygradlebuild.builder.GradleProjectBuilderForJavaProject;

public class GradleBuildFileGenerator {

    private final FileWriter fileWriter;

    public GradleBuildFileGenerator() {
        this.fileWriter = new FileWriter();
    }

    public void forJavaProject(final File file,
            final GradleProjectBuilderForJavaProject projectBuilder) {
        makingSureParentDirectoryExists(file, projectBuilder.projectName());
        final StringBuilder builder = new StringBuilder();
        projectBuilder.appendTo(builder);
        writeToFile(file, builder.toString(), "build.gradle");
    }

    private void writeToFile(final File parent, final String contents,
            final String buildFile) {
        try {
            fileWriter().writeToFile(parent, contents, buildFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forJavaLibProject(final File file,
            final GradleProjectBuilderForJavaLibraryProject projectBuilder) {
        makingSureParentDirectoryExists(file, projectBuilder.projectName());
        final StringBuilder builder = new StringBuilder();
        projectBuilder.appendTo(builder);
        writeToFile(file, builder.toString(), "build.gradle");
    }

    private FileWriter fileWriter() {
        return this.fileWriter;
    }

    private static void makingSureParentDirectoryExists(final File file,
            final String name) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException(
                        "Not able to create the parent directories for '"
                                + name + "'! Need to exit.");
            }
        }
    }
}
