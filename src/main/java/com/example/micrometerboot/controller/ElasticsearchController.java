package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.BlogDto;
import com.example.micrometerboot.dto.ResponseDto;
import com.example.micrometerboot.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/blog")
    public ResponseDto findAllBlog() {
        log.info("[BEG] findAllBlog");
        ResponseDto response = new ResponseDto();
        response.setData(elasticsearchService.findAllBlog());
        log.info("[END] findAllBlog");
        return response;
    }

    @GetMapping("/blog/id/{id}")
    public ResponseDto findBlogById(@PathVariable("id") String id) {
        log.info("[BEG] findBlogById");
        ResponseDto response = new ResponseDto();
        response.setData(elasticsearchService.findBlogById(id));
        log.info("[END] findBlogById");
        return response;
    }

    @GetMapping("/blog/title/{title}")
    public ResponseDto findBlogByTitle(@PathVariable("title") String title) {
        log.info("[BEG] findBlogByTitle");
        ResponseDto response = new ResponseDto();
        response.setData(elasticsearchService.findBlogByTitle(title));
        log.info("[END] findBlogByTitle");
        return response;
    }

    @PostMapping("/blog")
    public ResponseDto addBlog(@RequestBody BlogDto blogDto) {
        log.info("[BEG] addBlog");
        ResponseDto response = new ResponseDto();
        response.setData(elasticsearchService.addBlog(blogDto));
        log.info("[END] addBlog");
        return response;
    }

    @PutMapping("/blog/{id}")
    public ResponseDto modifyBlog(@PathVariable("id") String id, @RequestBody BlogDto blogDto) {
        log.info("[BEG] modifyBlog");
        ResponseDto response = new ResponseDto();
        response.setData(elasticsearchService.modifyBlog(id, blogDto));
        log.info("[END] modifyBlog");
        return response;
    }

    @DeleteMapping("/blog/{id}")
    public ResponseDto removeBlog(@PathVariable("id") String id) {
        log.info("[BEG] removeBlog");
        ResponseDto response = new ResponseDto();
        elasticsearchService.removeBlog(id);
        log.info("[END] removeBlog");
        return response;
    }

}
