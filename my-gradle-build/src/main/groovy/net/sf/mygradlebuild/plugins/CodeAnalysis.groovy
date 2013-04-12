package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

class CodeAnalysis implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("findbugs");
        project.plugins.apply("pmd");
        project.plugins.apply("jdepend");

        project.task("testCodeAnalysis", dependsOn: [
            "findbugsTest",
            "pmdTest",
            "jdependTest"
        ]) {
            group = 'Verification'
            description = 'Analyze test code with pmd/findbugs/jdepend'
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("testCodeAnalysis")
                outputFactory.withStyle(Style.Info).println("Test code analyzed for $project ")
            }
        }

        project.task("mainCodeAnalysis", dependsOn: [
            "findbugsMain",
            "pmdMain",
            "jdependMain"
        ]) {
            group = 'Verification'
            description = 'Analyze main code with pmd/findbugs/jdepend'
            doLast {
                def outputFactory = services.get(StyledTextOutputFactory).create("mainCodeAnalysis")
                outputFactory.withStyle(Style.Info).println("Main code analyzed for $project ")
            }
        }
    }
}
