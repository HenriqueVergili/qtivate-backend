package com.qtivate.server.exceptions;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException(String message) {super(message);}
}
