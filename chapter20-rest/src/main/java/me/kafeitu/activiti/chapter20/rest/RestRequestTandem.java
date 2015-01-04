package me.kafeitu.activiti.chapter20.rest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 一个完整的流程
 *
 * @author: Henry Yan
 */
public class RestRequestTandem {

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

        // 查询请假流程的最新版本ID
        JsonNode jsonNode = queryLastVersionOfLeaveProcess();
        ArrayNode data = (ArrayNode) jsonNode.get("data");
        JsonNode next = data.iterator().next();
        String latestVersionId = next.get("id").asText();
        System.out.println("latestVersionId=" + latestVersionId);

        // 获取流程的启动表单属性
        queryProcessStartForm(latestVersionId);

        // 启动流程
        jsonNode = startProcessInstance(latestVersionId);
        String processInstanceId = jsonNode.get("id").asText();

        // 读取流程实例的变量
        listProcessInstanceVariables(processInstanceId);

        // 查询候选任务 -> 部门领导审批
        jsonNode = queryCandidateTask("deptLeader");
        data = (ArrayNode) jsonNode.get("data");
        next = data.iterator().next();
        String deptTaskId = next.get("id").asText();
        String taskName = next.get("name").asText();
        assert taskName.equals("部门领导审批");

        // 签收任务 -> 部门领导审批
        claimTask(deptTaskId, "kermit");

        // 提交任务 -> 部门领导审批
        List<Map<String, String>> variables = new ArrayList<Map<String, String>>();
        Map<String, String> var = new HashMap<String, String>();
        var.put("name", "deptLeaderApproved");
        var.put("value", "true");
        var.put("type", "string");
        variables.add(var);
        completeTask(deptTaskId, taskName, variables);

        // 查询候选任务 -> 人事审批
        jsonNode = queryCandidateTask("hr");
        data = (ArrayNode) jsonNode.get("data");
        next = data.iterator().next();
        String hrTaskId = next.get("id").asText();
        taskName = next.get("name").asText();
        assert taskName.equals("人事审批");

        // 签收任务 -> 人事审批
        claimTask(hrTaskId, "lily");

        // 提交任务 -> 人事审批
        variables = new ArrayList<Map<String, String>>();
        var = new HashMap<String, String>();
        var.put("name", "hrApproved");
        var.put("value", "true");
        var.put("type", "string");
        variables.add(var);
        completeTask(hrTaskId, taskName, variables);

        // 查询任务 -> 销假
        jsonNode = queryAssignedTask("kermit");
        data = (ArrayNode) jsonNode.get("data");
        next = data.iterator().next();
        String reportBackTaskId = next.get("id").asText();
        taskName = next.get("name").asText();
        assert taskName.equals("销假");

        // 完成任务 -> 销假
        variables = new ArrayList<Map<String, String>>();
        var = new HashMap<String, String>();
        var.put("id", "reportBackDate");
        var.put("value", "2014-02-06");
        variables.add(var);
        completeTaskUseForm(reportBackTaskId, taskName, variables);

        // 查询历史流程实例
        queryHistoricProcessInstance();
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

    private static JsonNode startProcessInstance(String processDefinitionId) throws IOException {
        WebClient client = createClient("form/form-data");
        // 非常重要
        client.type("application/json;charset=UTF-8");
        Map<String, Object> parameters = new HashMap<String, Object>();
//        parameters.put("processDefinitionKey", "leave");
        parameters.put("processDefinitionId", processDefinitionId);

        List<Map<String, String>> variables = new ArrayList<Map<String, String>>();
        Map<String, String> var = new HashMap<String, String>();
        var.put("id", "startDate");
        var.put("value", "2014-02-03");
        variables.add(var);

        var = new HashMap<String, String>();
        var.put("id", "endDate");
        var.put("value", "2014-02-05");
        variables.add(var);

        var = new HashMap<String, String>();
        var.put("id", "reason");
        var.put("value", "旅游");
        variables.add(var);

        parameters.put("properties", variables);
        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parameters);
        printJsonString("启动请假流程", body);
        Response response = client.post(body);
        return printResult("启动请假流程", response);
    }

    /**
     * 删除已经部署的请假流程
     *
     * @return
     * @throws IOException
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
            String deploymentId = jsonNode.get("id").textValue();
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
     * @throws IOException
     */
    private static void queryDeployment() {
        WebClient client = createClient("repository/deployments");
        Response response = client.get();

        // 转换并输出响应结果
        printResult("查询Deployment", response);
    }

    /**
     * 查询全部流程
     *
     * @throws IOException
     */
    private static JsonNode queryProcessDefinitions() {
        WebClient client = createClient("repository/process-definitions");
        Response response = client.get();

        // 转换并输出响应结果
        return printResult("查询流程定义", response);
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
     * @throws IOException
     */
    private static void deployLeave() {
        WebClient client = createClient("repository/deployments");

        InputStream resourceAsStream = RestRequestTandem.class.getClassLoader().getResourceAsStream("diagrams/leave.bpmn");
        client.type("multipart/form-data");
        ContentDisposition cd = new ContentDisposition("form-data;name=bpmn;filename=leave.bpmn;");
        Attachment att = new Attachment("leave.bpmn", resourceAsStream, cd);
        MultipartBody body = new MultipartBody(att);
        Response response = client.post(body);

        // 转换并输出响应结果
        printResult("部署流程", response);
    }

    // 创建Client
    private static WebClient createClient(String uri) {
        WebClient client = WebClient.create(BASE_REST_URI + uri);
        String auth = "Basic " + Base64Utility.encode("kermit:kermit".getBytes());
        client.header("Authorization", auth);
        return client;
    }

    // 打印输出结果
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

    // 打印发送请求的JSON数据
    private static void printJsonString(String phase, String json) {
        System.out.println("\n+++ 发送请求[" + phase + "] +++");
        System.out.println(json);
    }

}
