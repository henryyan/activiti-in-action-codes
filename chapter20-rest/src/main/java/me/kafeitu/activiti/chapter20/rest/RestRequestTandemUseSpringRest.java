package me.kafeitu.activiti.chapter20.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个完整的流程
 *
 * @author: Henry Yan
 */
public class RestRequestTandemUseSpringRest {

    private static final String BASE_REST_URI = "http://localhost:8089/activiti-rest/service/";

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static boolean formateOutputJson = true;

    public static void main(String[] args) throws IOException {
        // 查询deployment
        queryDeployment();

        // 删除已经部署的请假流程
        deleteLeaveDeployment();

        // 部署流程
        deployLeave();

        // 查询deployment
        queryDeployment();

        // 查询全部流程
        queryProcessDefinitions();

        // deployLeave();
        startProcessInstance();
    }

    private static JsonNode queryHistoricProcessInstance() {
        WebClient client = createClient("history/historic-process-instances?includeProcessVariables=true");

        Response response = client.get();

        // 转换并输出响应结果
        return printResult("查询历史流程实例", response);
    }

    private static void completeTask(String taskId, String taskName, List<Map<String, String>> variables) throws IOException {
        WebClient client = createClient("runtime/tasks/" + taskId);
        // 非常重要
        client.type("application/json;charset=UTF-8");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("action", "complete");
        if (variables != null) {
            parameters.put("variables", variables);
        }

        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parameters);
        printJsonString("完成任务[" + taskId + "-" + taskName + "]", body);
        Response response = client.post(body);
        printResult("完成任务[" + taskId + "-" + taskName + "]", response);
    }

    private static void completeTaskUseForm(String taskId, String taskName, List<Map<String, String>> properties) throws IOException {
        WebClient client = createClient("form/form-data");
        // 非常重要
        client.type("application/json;charset=UTF-8");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskId", taskId);
        if (properties != null) {
            parameters.put("properties", properties);
        }

        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parameters);
        printJsonString("完成任务-Form[" + taskId + "-" + taskName + "]", body);
        Response response = client.post(body);
        printResult("完成任务-Form[" + taskId + "-" + taskName + "]", response);
    }

    private static void claimTask(String taskId, String userId) throws IOException {
        WebClient client = createClient("runtime/tasks/" + taskId);
        // 非常重要
        client.type("application/json;charset=UTF-8");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("action", "claim");
        parameters.put("assignee", userId);

        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parameters);
        printJsonString("签收任务[" + taskId + "]", body);
        Response response = client.post(body);
        printResult("签收任务[" + taskId + "]", response);
    }

    private static JsonNode queryAssignedTask(String userId) {
        WebClient client = createClient("runtime/tasks?assignee=" + userId);

        Response response = client.get();

        // 转换并输出响应结果
        return printResult("读取用户[" + userId + "]的任务", response);
    }

    private static JsonNode queryCandidateTask(String groupId) {
        WebClient client = createClient("runtime/tasks?candidateGroup=" + groupId);

        Response response = client.get();

        // 转换并输出响应结果
        return printResult("读取组[" + groupId + "]的任务", response);
    }

    private static void listProcessInstanceVariables(String processInstanceId) {
        WebClient client = createClient("runtime/process-instances/" + processInstanceId + "/variables");

        Response response = client.get();

        // 转换并输出响应结果
        printResult("读取流程实例[" + processInstanceId + "]的变量", response);
    }

    private static void queryProcessStartForm(String processDefinitionId) {
        WebClient client = createClient("form/form-data?processDefinitionId=" + processDefinitionId);

        Response response = client.get();

        // 转换并输出响应结果
        printResult("获取流程的启动表单属性", response);
    }

    private static JsonNode startProcessInstance() throws IOException {

        RestClient restTemplate = newRestClient();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("processDefinitionKey", "leave");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);

        String response = restTemplate.postForObject(BASE_REST_URI + "runtime/process-instances", request, String.class);

        System.out.println("启动。。。" + response);


        return null;
    }

    /**
     * 删除已经部署的请假流程
     *
     * @return
     * @throws java.io.IOException
     */
    private static void deleteLeaveDeployment() throws IOException {
        WebClient client = createClient("repository/deployments");
        Response response = client.get();
        printResult("查询leave.bpmn", response);

        response = client.get();
        InputStream stream = (InputStream) response.getEntity();
        JsonNode responseNode = objectMapper.readTree(stream);
        Iterator<JsonNode> elements = responseNode.elements();
        JsonNode next = elements.next();
        ArrayNode arrayNode = (ArrayNode) next;
        for (JsonNode jsonNode : arrayNode) {
            String deploymentId = jsonNode.get("id").asText();
            if (StringUtils.isBlank(deploymentId)) {
                continue;
            }
            String url = "/repository/deployments/" + deploymentId + "?cascade=true";
            client = createClient(url);
            response = client.delete();
            printResult("删除deployment", response);
        }
    }

    /**
     * 查询全部流程
     *
     * @throws java.io.IOException
     */
    private static void queryDeployment() {
        /*RestClient restTemplate = newRestClient();
        String forObject = restTemplate.getForObject(BASE_REST_URI + "repository/deployments", String.class);
        System.out.println(forObject);*/

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange
                (BASE_REST_URI + "repository/deployments", HttpMethod.GET, new HttpEntity<Object>(createHeaders("kermit", "kermit")), String.class);
        String body = exchange.getBody();
        System.out.println(body);
    }

    private static RestClient newRestClient() {
        return new RestClient("kermit", "kermit");
    }

    private static HttpHeaders createHeaders( final String username, final String password ){
        return new HttpHeaders(){
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
    }

    /**
     * 查询全部流程
     *
     * @throws java.io.IOException
     */
    private static JsonNode queryProcessDefinitions() {
        RestClient restTemplate = newRestClient();
        String forObject = restTemplate.getForObject(BASE_REST_URI + "repository/process-definitions", String.class);
        System.out.println(forObject);

        // 转换并输出响应结果
        return printResult("查询流程定义", forObject);
    }

    private static JsonNode queryLastVersionOfLeaveProcess() {
        WebClient client = createClient("repository/process-definitions?key=leave&latest=true");
        Response response = client.get();

        StringWriter writer = new StringWriter();
        InputStream stream = (InputStream) response.getEntity();
        try {
            IOUtils.copy(stream, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String respText = writer.toString();
        System.out.println(respText);

        response = client.get();
        // 转换并输出响应结果
        return printResult("查询最新版本的请假流程定义", response);
    }

    /**
     * 部署流程
     *
     * @throws java.io.IOException
     */
    private static void deployLeave() {

        RestClient restClient = newRestClient();

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(RestRequestTandemUseSpringRest.class.getClassLoader().getResource("diagrams/leave.bpmn").getFile()));
        parts.add("tenantId", "aaaa");

        String s = restClient.postForObject(BASE_REST_URI + "repository/deployments", parts, String.class);
        System.out.println("部署。。。。");
        System.out.println(s);

    }

    // 创建Client
    private static WebClient createClient(String uri) {
        WebClient client = WebClient.create(BASE_REST_URI + uri);
        String auth = "Basic " + Base64Utility.encode("kermit:kermit".getBytes());
        client.header("Authorization", auth);
        return client;
    }

    // 打印输出结果
    private static JsonNode printResult(String phase, String response) {
        System.out.println("\n=== " + phase + " ===");

        try {
            Object json = objectMapper.readValue(response, Object.class);
            String indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println(indented);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 打印发送请求的JSON数据
    private static void printJsonString(String phase, String json) {
        System.out.println("\n+++ 发送请求[" + phase + "] +++");
        System.out.println(json);
    }

    private static JsonNode printResult(String phase, Response response) {
        System.out.println("\n=== " + phase + " ===");

        try {
            InputStream stream = (InputStream) response.getEntity();
            int available = 0;
            available = stream.available();

            if (available == 0) {
                System.out.println("nothing returned, response code: " + response.getStatus());
                return null;
            }
            JsonNode responseNode = objectMapper.readTree(stream);
            if (formateOutputJson) {
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseNode));
            } else {
                System.out.println(objectMapper.writeValueAsString(responseNode));
            }
            return responseNode;
        } catch (IOException e) {
            System.err.println("catch an exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

class RestClient extends RestTemplate {
    public RestClient(String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(null, -1),
                new UsernamePasswordCredentials(username, password));
        HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}
