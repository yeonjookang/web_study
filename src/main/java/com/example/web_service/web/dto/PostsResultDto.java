package com.example.web_service.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsResultDto <T> {
    private T data;
}
