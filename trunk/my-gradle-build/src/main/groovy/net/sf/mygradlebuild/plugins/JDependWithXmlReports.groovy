package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

class JDependWithXmlReports implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.plugins.apply("jdepend");

        project.apply {
            [
                project.jdependMain,
                project.jdependTest
            ]*.reports {
                xml.enabled true
                text.enabled false
            }
        }
        project.jdependMain.doLast {
            File file = new File(project.jdepend.reportsDir, "main.xml");
            assert file.exists() && file.isFile(), "File '$file' must exist"
            def numberOfCycles = new XmlSlurper().parse(file).Cycles.Package.size()
            def outputFactory = services.get(StyledTextOutputFactory).create("jdependMain.doLast")
            if (numberOfCycles > 0) {
                outputFactory.withStyle(Style.Error).println("We have detected $numberOfCycles cycles. Checkout report file: $file.")
                assert numberOfCycles == 0, """We have detected $numberOfCycles cycles. Checkout report file: $file."""
            }
            else {
                outputFactory.withStyle(Style.Info).println("No cycles detected for project '${project.name}' . Good work!")
            }
        }

        project.jdependTest.doLast {
            File file = new File(project.jdepend.reportsDir, "test.xml");
            assert file.exists() && file.isFile(), "File '$file' must exist"
            def numberOfCycles = new XmlSlurper().parse(file).Cycles.Package.size()
            def outputFactory = services.get(StyledTextOutputFactory).create("jdependTest.doLast")
            if (numberOfCycles > 0) {
                outputFactory.withStyle(Style.Error).println("We have detected $numberOfCycles cycles. Checkout report file: $file.")
                //assert numberOfCycles == 0, """We have detected $numberOfCycles cycles. Checkout report file: $file."""
            }
            else {
                outputFactory.withStyle(Style.Info).println("No cycles detected for project '${project.name}' . Good work!")
            }
        }
    }
}
