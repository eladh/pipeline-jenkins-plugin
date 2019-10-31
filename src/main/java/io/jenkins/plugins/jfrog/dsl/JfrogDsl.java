package io.jenkins.plugins.jfrog.dsl;

import groovy.lang.Binding;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.cps.CpsScript;
import org.jenkinsci.plugins.workflow.cps.GlobalVariable;

import javax.annotation.Nonnull;


@Extension
public class JfrogDsl extends GlobalVariable {

    @Nonnull
    @Override
    public String getName() {
        return "Jfrog";
    }

    @Nonnull
    @Override
    public Object getValue(@Nonnull CpsScript cpsScript) {
        Binding binding = cpsScript.getBinding();
        Object jfrog;
        if (binding.hasVariable(getName())) {
            jfrog = binding.getVariable(getName());
        } else {
            jfrog = new JfrogGlobalInstance(cpsScript);
            binding.setVariable(getName(), jfrog);
        }
        return jfrog;
    }
}


