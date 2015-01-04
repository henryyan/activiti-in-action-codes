package me.kafeitu.activiti.chapter20.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.restlet.data.ChallengeScheme;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.Iterator;

/**
 * 利用Restlet读取访问REST API
 * @author: Henry Yan
 */
public class RestRequestByRestlet {

    private static String REST_URI = "http://localhost:8080/activiti-rest/service/management/properties";

    public static void main(String[] args) throws IOException {
        ClientResource resource = new ClientResource(REST_URI);

        // 设置Base Auth认证
        resource.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "kermit", "kermit");

        Representation representation = resource.get();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(representation.getStream());
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            System.out.println(fieldName + " : " + jsonNode.get(fieldName));
        }
    }

}
