package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.BlogDto;
import com.example.micrometerboot.elasticsearch.document.BlogDocument;
import com.example.micrometerboot.service.ElasticsearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ElasticsearchController.class)
class ElasticsearchControllerTest {

    static final String _ID1 = "TESTID1";
    static final String _ID2 = "TESTID2";
    static final String _TYPE = "API";
    static final String _TITLE = "title";
    static final String _CONTENTS = "contents";
    static final ObjectMapper _MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @MockBean
    ElasticsearchService elasticsearchService;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("블로그 전체 검색")
    void findAllBlog() throws Exception {
        // given
        BlogDocument blogDocument1 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        BlogDocument blogDocument2 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID2)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        given(elasticsearchService.findAllBlog()).willReturn(Arrays.asList(blogDocument1, blogDocument2));

        // when, then
        mvc.perform(get("/blog"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_TITLE)))
                .andExpect(content().string(containsString(_CONTENTS)));
        verify(elasticsearchService, times(1)).findAllBlog();
    }

    @Test
    @DisplayName("블로그 아이디 검색")
    void findBlogById() throws Exception {
        // given
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        given(elasticsearchService.findBlogById(any())).willReturn(blogDocument);

        // when, then
        mvc.perform(get("/blog/id/{id}", _ID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_TITLE)))
                .andExpect(content().string(containsString(_CONTENTS)));
        verify(elasticsearchService, times(1)).findBlogById(any());
    }

    @Test
    @DisplayName("블로그 제목 검색")
    void findBlogByTitle() throws Exception {
        // given
        BlogDocument blogDocument1 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        BlogDocument blogDocument2 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID2)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        given(elasticsearchService.findBlogByTitle(any())).willReturn(Arrays.asList(blogDocument1, blogDocument2));

        // when, then
        mvc.perform(get("/blog/title/{title}", _TITLE))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_TITLE)))
                .andExpect(content().string(containsString(_CONTENTS)));
        verify(elasticsearchService, times(1)).findBlogByTitle(any());
    }

    @Test
    @DisplayName("블로그 추가")
    void addBlog() throws Exception {
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        BlogDto blogDto = fixtureMonkey.giveMeBuilder(BlogDto.class)
                .set(javaGetter(BlogDto::getTitle), _TITLE)
                .set(javaGetter(BlogDto::getContents), _CONTENTS)
                .sample();
        given(elasticsearchService.addBlog(any())).willReturn(blogDocument);

        mvc.perform(post("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(blogDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_TITLE)))
                .andExpect(content().string(containsString(_CONTENTS)));
        verify(elasticsearchService, times(1)).addBlog(any());
    }

    @Test
    @DisplayName("블로그 수정")
    void modifyBlog() throws Exception {
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getType), _TYPE)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .set(javaGetter(BlogDocument::getVersion), 0)
                .sample();
        BlogDto blogDto = fixtureMonkey.giveMeBuilder(BlogDto.class)
                .set(javaGetter(BlogDto::getTitle), _TITLE)
                .set(javaGetter(BlogDto::getContents), _CONTENTS)
                .sample();
        given(elasticsearchService.modifyBlog(any(), any())).willReturn(blogDocument);

        mvc.perform(put("/blog/{id}", _ID1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(blogDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_TITLE)))
                .andExpect(content().string(containsString(_CONTENTS)));
        verify(elasticsearchService, times(1)).modifyBlog(any(), any());
    }

    @Test
    @DisplayName("블로그 삭제")
    void removeBlog() throws Exception {
        willDoNothing().given(elasticsearchService).removeBlog(any());

        mvc.perform(delete("/blog/{id}", _ID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        verify(elasticsearchService, times(1)).removeBlog(any());
    }

}
