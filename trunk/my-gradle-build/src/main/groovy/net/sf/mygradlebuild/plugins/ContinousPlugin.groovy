package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

class ContinousPlugin implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("findbugs");
        project.plugins.apply("pmd");
        project.plugins.apply("jdepend");

        project.task("continous", dependsOn: [
            "test",
            "findbugsMain",
            "pmdMain",
            "jdependMain"
        ]) {
            group = 'Verification'
            description = 'Continous build task including junit tests/pmd/findbugs/jdepend'
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("continous")
                outputFactory.withStyle(Style.Info).println("Continous build for $project passed. Good work!")
            }
        }
    }
}
