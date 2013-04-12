package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs

class FindbugsWithHtmlReports implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("findbugs");

        project.tasks.withType(FindBugs){ ignoreFailures = false }

        project.apply {
            [
                project.findbugsMain,
                project.findbugsTest
            ]*.reports {
                xml.enabled false
                html.enabled true
            }
        }
    }
}
