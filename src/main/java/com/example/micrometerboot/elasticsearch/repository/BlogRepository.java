package com.example.micrometerboot.elasticsearch.repository;

import com.example.micrometerboot.elasticsearch.document.BlogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("blogRepository")
public interface BlogRepository extends ElasticsearchRepository<BlogDocument, String> {
    List<BlogDocument> findByTitle(String title);

}
