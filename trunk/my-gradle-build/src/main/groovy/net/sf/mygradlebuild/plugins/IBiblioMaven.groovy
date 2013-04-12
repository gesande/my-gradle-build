package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class IBiblioMaven implements Plugin<Project>{

    @Override
    public void apply(final Project project) {
        project.allprojects {
            repositories { mavenRepo url: "mirrors.ibiblio.org/maven2/" }
        }
    }
}
