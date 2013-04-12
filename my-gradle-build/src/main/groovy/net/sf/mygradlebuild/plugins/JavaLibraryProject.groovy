package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaLibraryProject implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.plugins.apply("java")
        project.plugins.apply("eclipse")

        project.repositories { flatDir { dirs 'lib' } }

        project.dependencies {
            compile project.fileTree(dir: 'lib', include: '*.jar', exclude: '*-sources.jar')
        }

        project.gradle.projectsEvaluated {
            project.eclipse {
                classpath {
                    file {
                        whenMerged { classpath ->
                            classpath.entries.removeAll { entry ->
                                entry.kind == 'lib'
                            }
                        }
                        withXml {
                            def node = it.asNode()
                            def componentPath = "${project.projectDir}/lib/${project.properties.library}"
                            def componentSourcePath = "${project.projectDir}/lib-sources/${project.properties.librarySources}"
                            node.appendNode('classpathentry', [kind: 'lib', path: componentPath, exported: true, sourcepath: componentSourcePath ])
                            println "Forked classpath entry with source jar for ${project.properties.library}"
                        }
                    }
                }
            }
        }
    }
}
