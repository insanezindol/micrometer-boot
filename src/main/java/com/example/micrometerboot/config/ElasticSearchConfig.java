package com.example.micrometerboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * Spring Data Elasticsearch 5.x 부터 RestHighLevelClient / ElasticsearchRestTemplate 이 제거되어
 * 신규 Elasticsearch Java Client 기반의 ElasticsearchConfiguration 을 사용한다.
 * ElasticsearchClient, ElasticsearchOperations(elasticsearchTemplate) 빈은 상위 클래스가 등록해준다.
 */
@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .build();
    }

}
