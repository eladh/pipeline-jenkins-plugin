package io.jenkins.plugins.jfrog;

public class JenkinsBuildEvent {

    private String jenkinsJobName;
    private String status;
    private String jenkinsBuildUrl;
    private String jenkinsBuildId;
    private String jenkinsQueueId;
    private String outputs;

    public String getJenkinsJobName() {
        return jenkinsJobName;
    }

    void setJenkinsJobName(String jenkinsJobName) {
        this.jenkinsJobName = jenkinsJobName;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    public String getJenkinsBuildUrl() {
        return jenkinsBuildUrl;
    }

    void setJenkinsBuildUrl(String jenkinsBuildUrl) {
        this.jenkinsBuildUrl = jenkinsBuildUrl;
    }

    public String getJenkinsBuildId() {
        return jenkinsBuildId;
    }

    void setJenkinsBuildId(String jenkinsBuildId) {
        this.jenkinsBuildId = jenkinsBuildId;
    }

    public String getJenkinsQueueId() {
        return jenkinsQueueId;
    }

    void setJenkinsQueueId(String jenkinsQueueId) {
        this.jenkinsQueueId = jenkinsQueueId;
    }

    public String getOutputs() {
        return outputs;
    }

    void setOutputs(String outputs) {
        this.outputs = outputs;
    }
}
