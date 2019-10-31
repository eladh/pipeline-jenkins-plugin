package io.jenkins.plugins.jfrog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());

    static ObjectMapper getMapper() {
        return mapper;
    }

    static String writeValue(Object pojo) {
        try {
            return JsonUtils.getMapper().writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE ,e.getMessage());
            return null;
        }
    }
}
