package io.jenkins.plugins.jfrog.dsl;

import hudson.Extension;
import hudson.model.Run;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CreatePipelineStep extends Step {

    private static final Logger LOGGER = Logger.getLogger(CreatePipelineStep.class.getName());

    @DataBoundConstructor
    public CreatePipelineStep() { }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(context);
    }

    public static class Execution extends SynchronousStepExecution {
        private static final long serialVersionUID = 1L;

        private StepContext stepContext;

        protected Execution(@Nonnull StepContext context) {
            super(context);
            this.stepContext = context;
        }

        @Override
        protected Object run() {
            try {
                return new PipelineNotifier(stepContext.get(Run.class));
            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.SEVERE , e.getMessage());
                return null;
            }
        }
    }

    @Extension
    public static final class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(CreatePipelineStep.Execution.class);
        }

        @Override
        public String getFunctionName() {
            return "newPipelineNotifier";
        }

        @Override
        public String getDisplayName() {
            return "New PipelienNotifiner";
        }

        @Override
        public boolean isAdvanced() {
            return true;
        }
    }

}

