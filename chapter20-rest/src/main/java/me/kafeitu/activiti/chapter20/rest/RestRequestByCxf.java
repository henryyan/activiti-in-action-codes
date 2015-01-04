package me.kafeitu.activiti.chapter20.rest;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * 通过Apache CXF调用REST API
 * @author: Henry Yan
 */
public class RestRequestByCxf {

    private static String REST_URI = "http://localhost:8080/activiti-rest/service/management/properties";

    public static void main(String[] args) throws IOException {
        // 创建client对象
        WebClient client = WebClient.create(REST_URI);

        // Basic Auth身份认证
        String auth = "Basic " + Base64Utility.encode("kermit:kermit".getBytes());
        client.header("Authorization", auth);

        // 获取响应内容
        Response response = client.get();
        InputStream stream = (InputStream) response.getEntity();

        // 转换并输出响应结果
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        String respText = writer.toString();
        System.out.println(respText);
    }

}