package net.sf.mygradlebuild.plugins


import org.gradle.api.Plugin
import org.gradle.api.Project

class DefaultContinousBuildPlugin implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply(JavaProjectArtifactPlugin)
        project.plugins.apply(FindbugsWithHtmlReports)
        project.plugins.apply(ForkPmdSettings)
        project.plugins.apply(JDependWithXmlReports)
        project.plugins.apply(CodeAnalysis)
        project.plugins.apply(ContinousPlugin)
        project.plugins.apply(ForkEclipseJdtPlugin)
        project.plugins.apply(EclipseClasspathPlugin)
        project.plugins.apply(ProjectVersion)
    }
}
