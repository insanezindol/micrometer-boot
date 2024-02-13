package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.BlogDto;
import com.example.micrometerboot.elasticsearch.document.BlogDocument;
import com.example.micrometerboot.elasticsearch.repository.BlogRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final BlogRepository blogRepository;

    private static final String _PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Transactional(readOnly = true)
    public List<BlogDocument> findAllBlog() {
        return Lists.newArrayList(blogRepository.findAll(Sort.by(Sort.Direction.DESC, "logDate")));
    }

    @Transactional(readOnly = true)
    public BlogDocument findBlogById(String id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<BlogDocument> findBlogByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    @Transactional
    public BlogDocument addBlog(BlogDto blogDto) {
        BlogDocument blogDocument = new BlogDocument();
        blogDocument.setId(String.valueOf(UUID.randomUUID()));
        blogDocument.setType("API");
        blogDocument.setTitle(blogDto.getTitle());
        blogDocument.setContents(blogDto.getContents());
        blogDocument.setVersion(1);
        blogDocument.setLogDate(new SimpleDateFormat(_PATTERN).format(new Date()));
        return blogRepository.save(blogDocument);
    }

    @Transactional
    public BlogDocument modifyBlog(String id, BlogDto blogDto) {
        BlogDocument blogDocument = blogRepository.findById(id).orElse(null);
        Optional.ofNullable(blogDocument)
                .ifPresent(blog -> {
                    blog.setTitle(blogDto.getTitle());
                    blog.setContents(blogDto.getContents());
                    blog.setVersion(blog.getVersion() + 1);
                    blogRepository.save(blog);
                });
        return blogDocument;
    }

    @Transactional
    public void removeBlog(String id) {
        BlogDocument blogDocument = blogRepository.findById(id).orElse(null);
        Optional.ofNullable(blogDocument)
                .ifPresent(blog -> {
                    blogRepository.delete(blog);
                });
    }

}
