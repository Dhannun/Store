package com.touchit.foodlify.exceptions;

public class ResourceNotFoundException extends RuntimeException{
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
