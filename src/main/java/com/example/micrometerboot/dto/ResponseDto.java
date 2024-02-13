package com.example.micrometerboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {

    private int code;
    private String message;
    private Object data;

    public ResponseDto() {
        code = 0;
        message = "success";
        data = null;
    }

}
