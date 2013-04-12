package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.GradleBuild
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

class EclipseSettingsPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.plugins.apply("eclipse");

        project.task ("eclipseSettings", type: GradleBuild) { GradleBuild task ->
            group = 'IDE'
            description= 'Build fresh Eclipse settings for all projects.'
            tasks << 'eclipseSettingsFor'
            doLast {
                services.get(StyledTextOutputFactory).create("eclipseSettings.eclipseSettings").withStyle(Style.Info).println("Eclipse settings ready for all projects. Now import the projects (without copying them) into Eclipse.")
            }
        }
        project.subprojects { Project subproject ->
            subproject.task("eclipseSettingsFor", type: GradleBuild) { GradleBuild task->
                group = 'IDE'
                description = 'Build fresh Eclipse settings for a specific module.'
                tasks << 'cleanEclipse'
                tasks << 'eclipse'
                doLast {
                    services.get(StyledTextOutputFactory).create("eclipseSettings.eclipseSettingsFor").withStyle(Style.Info).println("Eclipse settings ready for ${task.project.name}. Now import the project (without copying it) into Eclipse.")
                }
            }
        }
    }
}
