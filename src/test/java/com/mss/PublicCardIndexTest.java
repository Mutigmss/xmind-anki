package com.mss;

//import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
//import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.mss.constant.PublicCardConstant.MAPPING_TEMPLATE;

public class PublicCardIndexTest {
    private RestHighLevelClient client;

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://8.134.24.173:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }



    @Test
    void createHotelIndex() throws IOException {
        // 1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("publiccard");
        // 2.准备请求的参数：DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);


    }


}