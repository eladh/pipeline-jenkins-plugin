package io.jenkins.plugins.jfrog;

import com.fasterxml.jackson.core.type.TypeReference;
import hudson.model.Result;
import hudson.model.Run;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PipelineUtils {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final Configuration.ConfigurationDescriptor descriptor = Configuration.DESCRIPTOR;

    private static final Logger LOGGER = Logger.getLogger(PipelineUtils.class.getName());

   public static Optional<String> addPipelineJenkinsIntegration(JenkinsIntegration integration) {
        StringEntity requestEntity = new StringEntity(Objects.requireNonNull(JsonUtils.writeValue(integration)), ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost(Configuration.DESCRIPTOR.getPipelineApiServerUrl() + "/projectIntegrations");
        postMethod.setHeader(HttpHeaders.AUTHORIZATION ,"apiToken " + Configuration.DESCRIPTOR.getPipelineServerToken());
        postMethod.setEntity(requestEntity);

        try {
            HttpResponse rawResponse = httpclient.execute(postMethod);
            String responseString = EntityUtils.toString(rawResponse.getEntity());
            Map<String, Object> integrationResponse = JsonUtils.getMapper().readValue(responseString, new TypeReference<Map<String, Object>>() {});
            LOGGER.log(Level.SEVERE ,responseString);
            return Optional.of(String.valueOf(integrationResponse.get("id")));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE ,e.getMessage());
        }

        return Optional.empty();
    }

    public static void notifyPipeline(Run build ,Result result) {
        JenkinsBuildEvent jenkinsBuildEvent = generateJenkinsBuildEvent(build);
        jenkinsBuildEvent.setStatus(getStatus(result).getStatus());

        StringEntity requestEntity = new StringEntity(Objects.requireNonNull(JsonUtils.writeValue(jenkinsBuildEvent)), ContentType.APPLICATION_JSON);
        HttpPost postMethod = new HttpPost(descriptor.getPipelineApiServerUrl() + "/projectIntegrations/" + descriptor.getPipelineWebHookId() + "/hook");
        postMethod.setHeader(HttpHeaders.AUTHORIZATION ,"basic " + generateAuthToken());
        postMethod.setEntity(requestEntity);

        try {
            httpclient.execute(postMethod);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE ,e.getMessage());
        }
    }


    private static JenkinsBuildEvent generateJenkinsBuildEvent(Run build) {
        JenkinsBuildEvent jenkinsBuildEvent = new JenkinsBuildEvent();
        jenkinsBuildEvent.setJenkinsJobName(build.getParent().getName());
        jenkinsBuildEvent.setJenkinsBuildId(build.getId());
        jenkinsBuildEvent.setJenkinsQueueId(String.valueOf(build.getQueueId()));
        jenkinsBuildEvent.setJenkinsBuildUrl(build.getUrl());
        jenkinsBuildEvent.setOutputs("{" +
                "  \"svc_build_info\": {" +
                "    \"buildName\":\"" +  build.getDisplayName() + "\"," +
                "    \"buildNumber\":\"" +  build.getId() + "\"" +
                "  }" +
                "}");

        Result result = build.getResult();
        jenkinsBuildEvent.setStatus(result != null ? getStatus(result).getStatus() : PipelineJobStatus.FAILURE.getStatus());

        return jenkinsBuildEvent;
    }


    private static String generateAuthToken() {
        String authToken = descriptor.getJenkinsUser() + ":" + descriptor.getJenkinsToken();
        return Base64.getEncoder().encodeToString(authToken.getBytes(Charset.forName("UTF-8")));
    }


    private static PipelineJobStatus getStatus(Result result) {
        String jenkinsStatus = result.toString();
        if (jenkinsStatus.equals(""))
            return PipelineJobStatus.PROCESSING;

        if (jenkinsStatus.equals("UNSTABLE") || jenkinsStatus.equals("FAILURE") || jenkinsStatus.equals("NOT_BUILT")
                || jenkinsStatus.equals("ABORTED"))
            return PipelineJobStatus.FAILURE;

        return PipelineJobStatus.SUCCESS;
    }
}
