package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.BlogDto;
import com.example.micrometerboot.elasticsearch.document.BlogDocument;
import com.example.micrometerboot.elasticsearch.repository.BlogRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ElasticsearchServiceTest {

    static final String _ID1 = "TESTID1";
    static final String _ID2 = "TESTID2";
    static final String _TITLE = "title";
    static final String _CONTENTS = "contents";

    ElasticsearchService elasticsearchService;

    FixtureMonkey fixtureMonkey;

    @Mock
    BlogRepository blogRepository;

    @BeforeEach
    void setup() {
        this.elasticsearchService = new ElasticsearchService(blogRepository);
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("블로그 전체 검색")
    void findAllBlog() {
        // given
        final int docSize = 5;
        List<BlogDocument> blogDocuments = fixtureMonkey.giveMe(BlogDocument.class, docSize);
        when(blogRepository.findAll((Sort) any())).thenReturn(blogDocuments);

        // when
        List<BlogDocument> list = elasticsearchService.findAllBlog();

        // then
        assertNotNull(list);
        assertEquals(docSize, list.size());
        verify(blogRepository, times(1)).findAll((Sort) any());
    }

    @Test
    @DisplayName("블로그 아이디 검색")
    void findBlogById() {
        // given
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .sample();
        when(blogRepository.findById(any())).thenReturn(Optional.of(blogDocument));

        // when
        BlogDocument blog = elasticsearchService.findBlogById(_ID1);

        // then
        Assertions.assertNotNull(blog);
        Assertions.assertEquals(_ID1, blog.getId());
        verify(blogRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("블로그 제목 검색")
    void findBlogByTitle() {
        // given
        BlogDocument blogDocument1 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .sample();
        BlogDocument blogDocument2 = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID2)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .sample();
        when(blogRepository.findByTitle(any())).thenReturn(Arrays.asList(blogDocument1, blogDocument2));

        // when
        List<BlogDocument> list = elasticsearchService.findBlogByTitle(_TITLE);

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
        assertThat(list).filteredOn("id", _ID1).containsOnly(blogDocument1);
        assertThat(list).filteredOn("id", _ID2).containsOnly(blogDocument2);
        assertThat(list).extracting("title").contains(_TITLE);
        verify(blogRepository, times(1)).findByTitle(any());
    }

    @Test
    @DisplayName("블로그 추가")
    void addBlog() {
        // given
        BlogDto blogDto = fixtureMonkey.giveMeBuilder(BlogDto.class)
                .set(javaGetter(BlogDto::getTitle), _TITLE)
                .set(javaGetter(BlogDto::getContents), _CONTENTS)
                .sample();
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .sample();
        when(blogRepository.save(any())).thenReturn(blogDocument);

        // when
        BlogDocument blog = elasticsearchService.addBlog(blogDto);

        // then
        Assertions.assertNotNull(blog);
        Assertions.assertEquals(blogDto.getTitle(), blog.getTitle());
        Assertions.assertEquals(blogDto.getContents(), blog.getContents());
        verify(blogRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("블로그 수정")
    void modifyBlog() {
        // given
        BlogDto blogDto = fixtureMonkey.giveMeBuilder(BlogDto.class)
                .set(javaGetter(BlogDto::getTitle), _TITLE)
                .set(javaGetter(BlogDto::getContents), _CONTENTS)
                .sample();
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class)
                .set(javaGetter(BlogDocument::getId), _ID1)
                .set(javaGetter(BlogDocument::getTitle), _TITLE)
                .set(javaGetter(BlogDocument::getContents), _CONTENTS)
                .sample();
        when(blogRepository.findById(any())).thenReturn(Optional.of(blogDocument));
        when(blogRepository.save(any())).thenReturn(blogDocument);

        // when
        BlogDocument blog = elasticsearchService.modifyBlog(_ID1, blogDto);

        // then
        Assertions.assertNotNull(blog);
        Assertions.assertEquals(blogDto.getTitle(), blog.getTitle());
        Assertions.assertEquals(blogDto.getContents(), blog.getContents());
        verify(blogRepository, times(1)).findById(any());
        verify(blogRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("블로그 삭제")
    void removeBlog() {
        // given
        BlogDocument blogDocument = fixtureMonkey.giveMeBuilder(BlogDocument.class).sample();
        when(blogRepository.findById(any())).thenReturn(Optional.of(blogDocument));

        // when
        elasticsearchService.removeBlog(_ID1);

        // then
        verify(blogRepository, times(1)).findById(any());
        verify(blogRepository, times(1)).delete(any());
    }

}
