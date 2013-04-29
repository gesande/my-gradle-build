package net.sf.mygradlebuild;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class AntBuildFileGeneratorForGradleTest {

    @SuppressWarnings("static-method")
    @Test
    public void writeBuildXml() throws IOException {
        final AntBuildFileGeneratorForGradle buildFileGenerator = new AntBuildFileGeneratorForGradle();
        final File parent = new File("target");
        buildFileGenerator.generate(parent, "build.xml", "defaultTarget",
                "defaultTarget", "target1");
        assertEquals(
                "<!-- NOTE this is generated but versioned file -->\n"
                        + "<project name=\"gradle\" default=\"defaultTarget\" basedir=\"..\\\">\n"
                        + "\t<description description=\"Build entrypoints\">\n"
                        + "\t</description>\n\t<!-- set global properties for this build -->\n"
                        + "\t<property environment=\"env\" />\n"
                        + "\t<condition property=\"gradle-exec\" value=\"gradle.bat\" else=\"gradle\">\n"
                        + "\t\t<os family=\"windows\" />\n"
                        + "\t</condition>\n"
                        + "\t<property name=\"gradle.executable\" location=\"${env.GRADLE_HOME}/bin/${gradle-exec}\" />\n"
                        + "\t<target name=\"defaultTarget\">\n"
                        + "\t\t<exec executable=\"${gradle.executable}\" dir=\".\">\n"
                        + "\t\t\t<arg value=\"defaultTarget\" />\n\t\t</exec>\n"
                        + "\t</target>\n"
                        + "\t<target name=\"target1\">\n\t\t<exec executable=\"${gradle.executable}\" dir=\".\">\n"
                        + "\t\t\t<arg value=\"target1\" />\n\t\t</exec>\n\t</target>\n</project>\n",
                readBuildFile(parent, "build.xml"));
    }

    private static String readBuildFile(final File parent, final String filename)
            throws FileNotFoundException, IOException {
        return ReadFileToStringBuilder.readBuildFile(parent, filename)
                .toString();
    }
}
