import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.GradleBuild


public class YourApplicationBuildPlugin implements Plugin<Project>{

    @Override
    public void apply(Project project) {

        project.task("continousBuild", type: GradleBuild) { Task task ->
            group = 'Verification'
            description ='Continous build for the whole thing. Works also as a license to commit build target.'
            buildFile = 'build.gradle'
            tasks << 'clean'
            tasks << 'eclipseSettings'
            tasks << 'applySvnIgnoreFromGeneratedFile'
            tasks << 'exportAntBuildFile'

            tasks << 'yourmodule:continous'
            tasks << 'yourmodule:dist'

            tasks << 'aggregateTestReport'
            tasks << 'aggregateJDependReport'
            tasks << 'aggregateCoverageReport'
            tasks << 'aggregateFindbugsReport'
            tasks << 'archiveAggregateReports'
            doLast { println "Continous build passed, good work!" }
        }

        project.task("distributionPackage", type: GradleBuild, dependsOn: ['continousBuild']) { Task task ->
            group = 'Distribution'
            description = 'Distribution package for the whole thing including continous build.'
            buildFile = 'build.gradle'
            tasks << 'yourmodule:release'
            tasks << 'makeDistributionPackage'
            doLast { println "Distribution package ready to be uploaded to the repository." }
        }
    }
}

