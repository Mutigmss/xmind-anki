package com.mss.chatgptmodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author ttpfx
 * @date 2023/3/28
 */
@Component
public class ChatModel {

    /**
     * 这里分别代表密钥，请求的地址，编码
     */
    private String apiKey="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";
    private String url="https://api.openai.com/v1/chat/completions";
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * 设置异步请求的客户端
     */
    private CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault();
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 该方法会异步请求chatGpt的接口，返回答案
     * 用于测试的，可以在控制台打印输出结果
     *
     * @param chatGptRequestParameter 请求的参数
     * @param question                问题
     */
    public void printAnswer(ChatRequestParameter chatGptRequestParameter, String question) {


        asyncClient.start();
        // 创建一个HttpPost
        AsyncRequestBuilder asyncRequest = AsyncRequestBuilder.post(url);
        // 创建一个ObjectMapper，用于解析和创建json

        // 设置请求参数
        chatGptRequestParameter.addMessages(new ChatMessage("user", question));

        String valueAsString = null;
        try {
            valueAsString = objectMapper.writeValueAsString(chatGptRequestParameter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(valueAsString);
        ContentType contentType = ContentType.create("text/plain", charset);
        asyncRequest.setEntity(valueAsString, contentType);

        asyncRequest.setCharset(charset);
        // 设置请求头
        asyncRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        // 设置登录凭证
        asyncRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        CountDownLatch latch = new CountDownLatch(1);

        StringBuilder sb = new StringBuilder();

        AbstractCharResponseConsumer<HttpResponse> consumer = new AbstractCharResponseConsumer<HttpResponse>() {

            HttpResponse response;

            @Override
            protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException {
                setCharset(charset);
                this.response = response;
            }

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer src, boolean endOfStream) throws IOException {
                String ss = src.toString();
                for (String s : ss.split("data:")) {
                    if (s.startsWith("data:")) {
                        s = s.substring(5);
                    }
                    if (s.length() > 10) {

                        ChatResponseParameter responseParameter = objectMapper.readValue(s, ChatResponseParameter.class);
                        for (Choice choice : responseParameter.getChoices()) {
                            String content = choice.getDelta().getContent();
                            if (content != null && !"".equals(content)) {
                                sb.append(content);
                                System.out.print(content);
                            }
                        }

                    }
                }

            }

            @Override
            protected HttpResponse buildResult() throws IOException {
                return response;
            }

            @Override
            public void releaseResources() {
            }
        };

        asyncClient.execute(asyncRequest.build(), consumer, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                latch.countDown();
                chatGptRequestParameter.addMessages(new ChatMessage("assistant", sb.toString()));
                System.out.println("回答结束！！！");
            }

            @Override
            public void failed(Exception ex) {
                latch.countDown();
                System.out.println("failed");
                ex.printStackTrace();
            }

            @Override
            public void cancelled() {
                latch.countDown();
                System.out.println("cancelled");
            }

        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
