package com.example.micrometerboot.client;

import com.example.micrometerboot.config.FeignConfig;
import com.example.micrometerboot.dto.ProductDto;
import com.example.micrometerboot.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "feign-sample-api", url = "${wiremock.host}:${wiremock.port}", configuration = FeignConfig.class)
public interface MockFeignClient {

    String _X_API_KEY = "x-api-key";

    @RequestMapping(method = RequestMethod.GET, value = "/v1/get-test")
    List<ProductDto> findAllProduct(@RequestHeader(_X_API_KEY) String apiKey);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/get-test/{order-id}")
    ProductDto findProduct(@RequestHeader(_X_API_KEY) String apiKey, @PathVariable("order-id") String orderId);

    @RequestMapping(method = RequestMethod.POST, value = "/v1/post-test")
    ResponseDto addProduct(@RequestHeader(_X_API_KEY) String apiKey, @RequestBody ProductDto productDto);

}
