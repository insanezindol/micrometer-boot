package com.example.micrometerboot.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.HttpHeaders;

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7");
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7");
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(host)
                .withDefaultHeaders(httpHeaders)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}