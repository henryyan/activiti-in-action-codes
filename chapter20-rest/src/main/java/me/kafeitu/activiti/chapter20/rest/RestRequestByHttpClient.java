package me.kafeitu.activiti.chapter20.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 通过HTTP Client访问REST API
 * @author: Henry Yan
 */
public class RestRequestByHttpClient {

    private static String REST_URI = "http://localhost:8080/activiti-rest/service/management/properties";

    public static void main(String[] args) throws IOException {

        // 设置Base Auth验证信息
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("kermit", "kermit");
        provider.setCredentials(AuthScope.ANY, credentials);

        // 创建HttpClient
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        // 发送请求
        HttpResponse response = client.execute(new HttpGet(REST_URI));
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity, "UTF-8");
            System.out.println(content);
        }
    }

}
