package com.btbatux.dream_shops.exception;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);

    }
}
