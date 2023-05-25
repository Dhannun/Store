package com.touchit.foodlify.exceptions;

public class OperationFailedException extends RuntimeException {
  public OperationFailedException(String message){
    super(message);
  }
}
