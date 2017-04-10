package net.sf.mygradlebuild.builder;

import java.util.ArrayList;
import java.util.List;

public class GradleProjectBuilderForJavaProject implements GradleProjectBuilder {

    private String projectName;
    private List<String> applyFroms;
    private List<String> applyPlugins;

    private GradleProjectBuilderForJavaProject() {
        this.applyFroms = new ArrayList<>();
        this.applyPlugins = new ArrayList<>();
    }

    public static GradleProjectBuilderForJavaProject forProject(
            final String projectName) {
        return new GradleProjectBuilderForJavaProject()
                .projectName(projectName);
    }

    private GradleProjectBuilderForJavaProject projectName(
            final String projectName) {
        this.projectName = projectName;
        return this;
    }

    public GradleProjectBuilderForJavaProject applyFroms(
            final String... applyFroms) {
        for (final String applyFrom : applyFroms) {
            applyFroms().add("apply from : " + applyFrom);
        }
        return this;
    }

    public GradleProjectBuilderForJavaProject applyPlugins(
            final String... plugins) {
        for (final String plugin : plugins) {
            applyPlugins().add("apply plugin : " + plugin);
        }
        return this;
    }

    @Override
    public void appendTo(final StringBuilder builder) {
        builder.append("project(':").append(projectName()).append("') {")
                .append("\n");
        for (final String applyFrom : applyFroms()) {
            builder.append(tab()).append(applyFrom).append("\n");
        }
        for (final String applyPlugin : applyPlugins()) {
            builder.append(tab()).append(applyPlugin).append("\n");
        }
        builder.append("}");

    }

    private List<String> applyPlugins() {
        return this.applyPlugins;
    }

    private List<String> applyFroms() {
        return this.applyFroms;
    }

    private static String tab() {
        return "    ";
    }

    public String projectName() {
        return this.projectName;
    }
}
