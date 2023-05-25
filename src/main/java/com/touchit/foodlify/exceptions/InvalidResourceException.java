package com.touchit.foodlify.exceptions;

public class InvalidResourceException extends RuntimeException{
  public InvalidResourceException(String message) {
    super(message);
  }
}
