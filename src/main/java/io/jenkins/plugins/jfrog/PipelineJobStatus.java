package io.jenkins.plugins.jfrog;

public enum PipelineJobStatus {

    PROCESSING("processing"),
    SUCCESS("success"),
    FAILURE("failure");

    private String status;

    PipelineJobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
