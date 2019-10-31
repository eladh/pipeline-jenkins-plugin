package io.jenkins.plugins.jfrog.dsl;

import hudson.model.Result;
import hudson.model.Run;
import io.jenkins.plugins.jfrog.PipelineUtils;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

import java.io.Serializable;

class PipelineNotifier implements Serializable {

    private Run build;

    PipelineNotifier(Run run) {
        this.build = run;
    }

    @Whitelisted
    public void sendJenkinsJob(String result) {
        PipelineUtils.notifyPipeline(this.build , Result.fromString(result));
    }
}
