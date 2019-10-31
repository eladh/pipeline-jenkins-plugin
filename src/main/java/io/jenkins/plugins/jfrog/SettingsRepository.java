package io.jenkins.plugins.jfrog;

public interface SettingsRepository {

    String getPipelineApiServerUrl();
    String getPipelineServerToken();
    String getJenkinsUser();
    String getPipelineWebHookId();
    String getJenkinsToken();
}
