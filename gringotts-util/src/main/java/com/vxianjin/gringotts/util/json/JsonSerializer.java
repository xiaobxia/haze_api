package com.vxianjin.gringotts.util.json;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonSerializer {

    public static String serializer(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        String resultStr = "";
        try {
            resultStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public static <T> T deserializer(String jsonStr, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        T o = null;
        try {
            mapper.configure(Feature.ALLOW_COMMENTS, true);
            mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            o = mapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }
}

