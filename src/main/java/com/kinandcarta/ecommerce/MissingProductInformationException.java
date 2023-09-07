package com.kinandcarta.ecommerce;

public class MissingProductInformationException extends RuntimeException{
    public MissingProductInformationException(String message) {
        super(message);
    }
}
