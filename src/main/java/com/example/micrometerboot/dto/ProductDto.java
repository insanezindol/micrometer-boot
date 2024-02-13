package com.example.micrometerboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("requested_at")
    private String requestedAt;

    @JsonProperty("canceled")
    private boolean canceled;

    @JsonProperty("fee")
    private int fee;

}
