package io.jenkins.plugins.jfrog;


import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.User;
import jenkins.model.Jenkins;
import jenkins.security.ApiTokenProperty;
import jenkins.security.apitoken.ApiTokenStore;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.accmod.restrictions.suppressions.SuppressRestrictedWarnings;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("WeakerAccess")
public class Configuration extends AbstractDescribableImpl<Configuration> {

    @Extension
    public static final ConfigurationDescriptor DESCRIPTOR = new ConfigurationDescriptor();
    public static final String PIPELINE_JENKINS_TOKEN = "pipeline-jenkins-token";

    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    private static final Jenkins JENKINS = Jenkins.getInstanceOrNull();


    @DataBoundConstructor
    public Configuration() { }

    @Override
    public ConfigurationDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @SuppressRestrictedWarnings(value = ApiTokenStore.class)
    @SuppressWarnings("unused")
    public static final class ConfigurationDescriptor extends Descriptor<Configuration> implements SettingsRepository {

        private String pipelineApiServerUrl;
        private String pipelineServerToken;
        private String pipelineWebHookId;
        private String jenkinsUser;
        private String jenkinsToken;

        public String getPipelineApiServerUrl() {
            return pipelineApiServerUrl;
        }

        @Override
        public String getPipelineServerToken() {
            return pipelineServerToken;
        }

        @Override
        public String getJenkinsUser() {
            return jenkinsUser;
        }

        @Override
        public String getJenkinsToken() {
            return jenkinsToken;
        }

        @Override
        public String getPipelineWebHookId() {
            return pipelineWebHookId;
        }

        public ConfigurationDescriptor() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Jfrog Pipeline setting";
        }


        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            pipelineApiServerUrl = StringUtils.trimToNull(formData.getString("pipelineApiServerUrl"));
            pipelineServerToken = StringUtils.trimToNull(formData.getString("pipelineServerToken"));
            jenkinsUser = StringUtils.trimToNull(formData.getString("jenkinsUser"));

            generateJenkinsToken();

            Optional<String> optionalId = PipelineUtils.addPipelineJenkinsIntegration(generatePipelineIntegration());
            optionalId.ifPresent(id -> pipelineWebHookId = id);
            save();

            return super.configure(req, formData);
        }

        private void generateJenkinsToken() {
            User user = User.get(getJenkinsUser() , false ,Collections.emptyMap());
            ApiTokenProperty prop = Objects.requireNonNull(user).getProperty(ApiTokenProperty.class);

            if (prop.getTokenStore().getTokenListSortedByName().stream().noneMatch(hashedToken -> hashedToken.getName().equals(PIPELINE_JENKINS_TOKEN))) {
                ApiTokenStore.TokenUuidAndPlainValue tokenUuidAndPlainValue = prop.getTokenStore().generateNewToken(PIPELINE_JENKINS_TOKEN);
                jenkinsToken = tokenUuidAndPlainValue.plainValue;

                LOGGER.log(Level.INFO ,"generate token :" + tokenUuidAndPlainValue.plainValue);
                try {
                    user.save();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE ,e.getMessage());
                }
            }
        }

        private JenkinsIntegration generatePipelineIntegration() {
            JenkinsIntegration jenkinsIntegration = new JenkinsIntegration();

            jenkinsIntegration.setName("jenkins_"+ getDomainName(JENKINS.getRootUrl()));
            jenkinsIntegration.setProjectId("1");
            jenkinsIntegration.setMasterIntegrationId("101");
            jenkinsIntegration.setMasterIntegrationType("generic");
            jenkinsIntegration.setMasterIntegrationName("jenkins");

            jenkinsIntegration.getFormJSONValues().add(new LabelValuePair("url" ,JENKINS.getRootUrl()));
            jenkinsIntegration.getFormJSONValues().add(new LabelValuePair("apiToken" ,jenkinsToken));
            jenkinsIntegration.getFormJSONValues().add(new LabelValuePair("username" ,getJenkinsUser()));

            return jenkinsIntegration;
        }
    }

    private static String getDomainName(String url)  {
        try {
            URI uri = new URI(url);
            return uri.getHost().replaceAll("\\." ,"_");
        } catch (URISyntaxException e) {
            return "";
        }
    }
}
