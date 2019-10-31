package io.jenkins.plugins.jfrog.dsl;

import com.google.common.collect.Maps;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;
import org.jenkinsci.plugins.workflow.cps.CpsScript;

import java.io.Serializable;

public class JfrogGlobalInstance implements Serializable {

    private CpsScript cpsScript;

    JfrogGlobalInstance(CpsScript cpsScript) {
        this.cpsScript = cpsScript;
    }

    @Whitelisted
    public Object newPipelineNotifier() {
        return cpsScript.invokeMethod("newPipelineNotifier", Maps.newLinkedHashMap());
    }
}