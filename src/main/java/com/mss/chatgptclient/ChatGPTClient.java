package com.mss.chatgptclient;

//import cn.hutool.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class ChatGPTClient {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public  String generateResponse(String prompt) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(API_URL);

        // Set headers
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " +API_KEY);

        // Set request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);
        requestBody.put("n", 1);
        requestBody.put("stop", "\n");
        StringEntity params = new StringEntity(requestBody.toString());
        request.setEntity(params);

        // Send request
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        // Read response
        if (entity != null) {
            String result = entity.getContent().toString();
            System.out.println(result);
            JSONObject responseJson = new JSONObject(result);
            System.out.println(responseJson.toString());
            return responseJson.getJSONArray("choices")
                    .getJSONObject(0).getString("text");
        } else {
            return null;
        }
    }
}