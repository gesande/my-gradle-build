package net.sf.mygradlebuild.builder;

import java.util.ArrayList;
import java.util.List;

public class GradleProjectBuilderForJavaLibraryProject implements
        GradleProjectBuilder {

    private String projectName;
    private List<String> applyFroms;
    private List<String> libraryEntries;

    private GradleProjectBuilderForJavaLibraryProject() {
        this.applyFroms = new ArrayList<String>();
        this.libraryEntries = new ArrayList<String>(2);
    }

    private GradleProjectBuilderForJavaLibraryProject projectName(
            final String name) {
        this.projectName = name;
        return this;
    }

    public GradleProjectBuilderForJavaLibraryProject applyFroms(
            final String... applyFroms) {
        for (final String applyFrom : applyFroms) {
            applyFroms().add("apply from : " + applyFrom);
        }
        return this;
    }

    public GradleProjectBuilderForJavaLibraryProject library(
            final String library) {
        libraryEntries().add("prj.ext.library = '" + library + ".jar'");
        libraryEntries().add(
                "prj.ext.librarySources = '" + library + "-sources.jar'");
        return this;
    }

    public String projectName() {
        return this.projectName;
    }

    @Override
    public void appendTo(final StringBuilder builder) {
        builder.append("project(':").append(projectName())
                .append("') { prj ->").append("\n");

        for (final String applyFrom : applyFroms()) {
            builder.append(tab()).append(applyFrom).append("\n");
        }

        for (final String libraryEntry : libraryEntries()) {
            builder.append(tab()).append(libraryEntry).append("\n");
        }
        builder.append("}");
    }

    private List<String> libraryEntries() {
        return this.libraryEntries;
    }

    private List<String> applyFroms() {
        return this.applyFroms;
    }

    private static String tab() {
        return "    ";
    }

    public static GradleProjectBuilderForJavaLibraryProject forProject(
            final String projectName) {
        return new GradleProjectBuilderForJavaLibraryProject()
                .projectName(projectName);
    }

}
