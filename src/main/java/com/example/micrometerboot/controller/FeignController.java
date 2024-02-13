package com.example.micrometerboot.controller;

import com.example.micrometerboot.client.MockFeignClient;
import com.example.micrometerboot.dto.ProductDto;
import com.example.micrometerboot.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FeignController {

    private final MockFeignClient mockFeignClient;

    @Value("${wiremock.api-key}")
    private String _API_KEY;

    @GetMapping("/product")
    public ResponseDto findAllProduct() {
        log.info("[BEG] findAllProduct");
        ResponseDto response = new ResponseDto();
        response.setData(mockFeignClient.findAllProduct(_API_KEY));
        log.info("[END] findAllProduct");
        return response;
    }

    @GetMapping("/product/id/{id}")
    public ResponseDto findProductById(@PathVariable("id") String id) {
        log.info("[BEG] findProductById");
        ResponseDto response = new ResponseDto();
        response.setData(mockFeignClient.findProduct(_API_KEY, id));
        log.info("[END] findProductById");
        return response;
    }

    @PostMapping("/product")
    public ResponseDto addProduct(@RequestBody ProductDto productDto) {
        log.info("[BEG] addProduct");
        ResponseDto response = new ResponseDto();
        response.setData(mockFeignClient.addProduct(_API_KEY, productDto));
        log.info("[END] addProduct");
        return response;
    }

}
