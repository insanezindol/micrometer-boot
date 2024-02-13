package com.example.micrometerboot.client;

import com.example.micrometerboot.dto.ProductDto;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

@WireMockTest
public class WireMockServerTest {

    @Test
    void simpleStubTesting(WireMockRuntimeInfo wmRuntimeInfo) {
        // given
        String responseBody = "Hello World !!";
        String apiUrl = "/api-url";
        stubFor(get(apiUrl).willReturn(ok(responseBody)));

        // when
        String apiResponse = getContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl);

        // then
        assertEquals(apiResponse, responseBody);
        verify(getRequestedFor(urlEqualTo(apiUrl)));
    }

    @Test
    void findAllProduct(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        // given
        JSONObject order1 = new JSONObject();
        order1.put("order_id", "ORD-11111");
        order1.put("user_id", "user11111");
        order1.put("product_id", "SHOES11111");
        order1.put("product_name", "NIKE SHOES");
        order1.put("requested_at", "2022-12-07T00:00:00.000Z");
        order1.put("canceled", false);
        order1.put("fee", 3000);
        JSONObject order2 = new JSONObject();
        order2.put("order_id", "ORD-22222");
        order2.put("user_id", "user22222");
        order2.put("product_id", "SHOES22222");
        order2.put("product_name", "ADIDAS SHOES");
        order2.put("requested_at", "2022-12-10T00:00:0.000Z");
        order2.put("canceled", false);
        order2.put("fee", 3000);
        JSONArray orderArray = new JSONArray();
        orderArray.put(order1);
        orderArray.put(order2);

        String apiUrl = "/v1/get-test";
        stubFor(get(urlEqualTo(apiUrl))
                .willReturn(aResponse()
                        .withFixedDelay(100)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(orderArray.toString())));

        // when
        String apiResponse = getContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl);

        // then
        assertEquals(apiResponse, orderArray.toString());
        verify(getRequestedFor(urlEqualTo(apiUrl)));
    }

    @Test
    void findProduct(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        // given
        String orderId = "ORD-11111";
        JSONObject order = new JSONObject();
        order.put("order_id", orderId);
        order.put("user_id", "user11111");
        order.put("product_id", "SHOES11111");
        order.put("product_name", "NIKE SHOES");
        order.put("requested_at", "2022-12-07T00:00:00.000Z");
        order.put("canceled", false);
        order.put("fee", 3000);

        String apiUrl = "/v1/get-test/";
        stubFor(get(urlMatching(apiUrl + "([0-9a-zA-Z\\-]*)"))
                .willReturn(aResponse()
                        .withFixedDelay(100)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(order.toString())));

        // when
        String apiResponse = getContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl + orderId);

        // then
        assertEquals(apiResponse, order.toString());
        verify(getRequestedFor(urlEqualTo(apiUrl + orderId)));
    }

    @Test
    void addProduct(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        // given
        JSONObject response = new JSONObject();
        response.put("code", 0);
        response.put("message", "OK");
        response.put("data", "success SHOES");

        String apiUrl = "/v1/post-test";
        stubFor(post(urlEqualTo(apiUrl))
                .willReturn(aResponse()
                        .withFixedDelay(100)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response.toString())));
        ProductDto productDto = new ProductDto();
        productDto.setProductId("test-order-id");

        // when
        String apiResponse = postContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl, productDto);

        // then
        assertEquals(apiResponse, response.toString());
        verify(postRequestedFor(urlEqualTo(apiUrl)));
    }

    private String getContent(String url) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        return testRestTemplate.getForObject(url, String.class);
    }

    private String postContent(String url, Object obj) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        return testRestTemplate.postForEntity(url, obj, String.class).getBody();
    }

}
