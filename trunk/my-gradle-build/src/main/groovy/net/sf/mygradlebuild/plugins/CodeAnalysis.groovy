package net.sf.mygradlebuild.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

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
			doLast { println("Test code analyzed for $project ") }
		}

		project.task("mainCodeAnalysis", dependsOn: [
			"findbugsMain",
			"pmdMain",
			"jdependMain"
		]) {
			group = 'Verification'
			description = 'Analyze main code with pmd/findbugs/jdepend'
			doLast { println("Main code analyzed for $project ") }
		}
	}
}
