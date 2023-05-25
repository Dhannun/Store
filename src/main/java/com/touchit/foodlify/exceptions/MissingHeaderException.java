package com.touchit.foodlify.exceptions;

public class MissingHeaderException extends RuntimeException{
  public MissingHeaderException(String message) {
    super(message);
  }
}
