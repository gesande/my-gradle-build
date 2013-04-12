package net.sf.mygradlebuild.tasks

import org.gradle.api.tasks.Exec;


public class BashExec extends Exec {

    BashExec() {
        executable="bash"
        args("-c")
    }
}
