package com.example.micrometerboot.controller;

import com.example.micrometerboot.client.MockFeignClient;
import com.example.micrometerboot.dto.ProductDto;
import com.example.micrometerboot.dto.ResponseDto;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeignController.class)
class FeignControllerTest {

    static final String _ID1 = "1";
    static final String _ID2 = "2";
    static final String _USER_ID = "user11111";
    static final String _PRODUCT_ID = "SHOES11111";
    static final String _PRODUCT_NAME = "NIKE SHOES";
    static final String _REQUESTED_AT = "2022-12-07T00:00:00.000Z";
    static final boolean _CACELED = false;
    static final int _FEE = 3000;
    static final ObjectMapper _MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @MockBean
    MockFeignClient mockFeignClient;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("상품 전체 검색")
    void findAllProduct() throws Exception {
        // given
        ProductDto productDto1 = fixtureMonkey.giveMeBuilder(ProductDto.class)
                .set(javaGetter(ProductDto::getOrderId), _ID1)
                .set(javaGetter(ProductDto::getUserId), _USER_ID)
                .set(javaGetter(ProductDto::getProductId), _PRODUCT_ID)
                .set(javaGetter(ProductDto::getProductName), _PRODUCT_NAME)
                .set(javaGetter(ProductDto::getRequestedAt), _REQUESTED_AT)
                .set(javaGetter(ProductDto::isCanceled), _CACELED)
                .set(javaGetter(ProductDto::getFee), _FEE)
                .sample();
        ProductDto productDto2 = fixtureMonkey.giveMeBuilder(ProductDto.class)
                .set(javaGetter(ProductDto::getOrderId), _ID2)
                .set(javaGetter(ProductDto::getUserId), _USER_ID)
                .set(javaGetter(ProductDto::getProductId), _PRODUCT_ID)
                .set(javaGetter(ProductDto::getProductName), _PRODUCT_NAME)
                .set(javaGetter(ProductDto::getRequestedAt), _REQUESTED_AT)
                .set(javaGetter(ProductDto::isCanceled), _CACELED)
                .set(javaGetter(ProductDto::getFee), _FEE)
                .sample();
        given(mockFeignClient.findAllProduct(any())).willReturn(Arrays.asList(productDto1, productDto2));

        // when, then
        mvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_USER_ID)))
                .andExpect(content().string(containsString(_PRODUCT_ID)))
                .andExpect(content().string(containsString(_PRODUCT_NAME)))
                .andExpect(content().string(containsString(_REQUESTED_AT)));
        verify(mockFeignClient, times(1)).findAllProduct(any());
    }

    @Test
    @DisplayName("상품 아이디 검색")
    void findProductById() throws Exception {
        // given
        ProductDto productDto = fixtureMonkey.giveMeBuilder(ProductDto.class)
                .set(javaGetter(ProductDto::getOrderId), _ID1)
                .set(javaGetter(ProductDto::getUserId), _USER_ID)
                .set(javaGetter(ProductDto::getProductId), _PRODUCT_ID)
                .set(javaGetter(ProductDto::getProductName), _PRODUCT_NAME)
                .set(javaGetter(ProductDto::getRequestedAt), _REQUESTED_AT)
                .set(javaGetter(ProductDto::isCanceled), _CACELED)
                .set(javaGetter(ProductDto::getFee), _FEE)
                .sample();
        given(mockFeignClient.findProduct(any(), any())).willReturn(productDto);

        // when, then
        mvc.perform(get("/product/id/{id}", _ID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_USER_ID)))
                .andExpect(content().string(containsString(_PRODUCT_ID)))
                .andExpect(content().string(containsString(_PRODUCT_NAME)))
                .andExpect(content().string(containsString(_REQUESTED_AT)));
        verify(mockFeignClient, times(1)).findProduct(any(), any());
    }

    @Test
    @DisplayName("상품 등록")
    void addProduct() throws Exception {
        // given
        ProductDto productDto = fixtureMonkey.giveMeBuilder(ProductDto.class)
                .set(javaGetter(ProductDto::getOrderId), _ID1)
                .set(javaGetter(ProductDto::getUserId), _USER_ID)
                .set(javaGetter(ProductDto::getProductId), _PRODUCT_ID)
                .set(javaGetter(ProductDto::getProductName), _PRODUCT_NAME)
                .set(javaGetter(ProductDto::getRequestedAt), _REQUESTED_AT)
                .set(javaGetter(ProductDto::isCanceled), _CACELED)
                .set(javaGetter(ProductDto::getFee), _FEE)
                .sample();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(0);
        responseDto.setMessage("OK");
        responseDto.setData("success");
        given(mockFeignClient.addProduct(any(), any())).willReturn(responseDto);

        // when, then
        mvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        verify(mockFeignClient, times(1)).addProduct(any(), any());
    }

}
