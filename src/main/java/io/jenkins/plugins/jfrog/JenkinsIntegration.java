package io.jenkins.plugins.jfrog;

import java.util.ArrayList;


public class JenkinsIntegration {

    private String name;
    private String projectId;
    private String masterIntegrationId;
    private String masterIntegrationName;
    private String masterIntegrationType;
    private ArrayList<LabelValuePair> formJSONValues = new ArrayList<>();


    public String getName() {
        return name;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getMasterIntegrationId() {
        return masterIntegrationId;
    }

    public String getMasterIntegrationName() {
        return masterIntegrationName;
    }

    public String getMasterIntegrationType() {
        return masterIntegrationType;
    }

    public ArrayList<LabelValuePair> getFormJSONValues() {
        return formJSONValues;
    }

    void setName(String name) {
        this.name = name;
    }

    void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    void setMasterIntegrationId(String masterIntegrationId) {
        this.masterIntegrationId = masterIntegrationId;
    }

    void setMasterIntegrationName(String masterIntegrationName) {
        this.masterIntegrationName = masterIntegrationName;
    }

    void setMasterIntegrationType(String masterIntegrationType) {
        this.masterIntegrationType = masterIntegrationType;
    }

    public void setFormJSONValues(ArrayList<LabelValuePair> formJSONValues) {
        this.formJSONValues = formJSONValues;
    }
}

class LabelValuePair {
    private String label;
    private String value;


    LabelValuePair(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}