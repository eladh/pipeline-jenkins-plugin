package io.jenkins.plugins.jfrog;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class NotifyPipelineServer extends Notifier {

    private static final Logger LOGGER = Logger.getLogger(NotifyPipelineServer.class.getName());

    @DataBoundConstructor
    public NotifyPipelineServer() { }

    @Override
    public boolean perform(@SuppressWarnings("rawtypes") final AbstractBuild build, final Launcher launcher, final BuildListener listener) {
        PipelineUtils.notifyPipeline(build ,build.getResult());
        return true;
    }


    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }


    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        @Nonnull
        public String getDisplayName() {
            return "Notify Job to Pipeline Server";
        }
    }

}
