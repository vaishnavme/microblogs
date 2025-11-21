package com.vaishnavs.microblogs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
  private String message;
  private Object data;

  public ErrorDetails(String message) {
    this.message = message;
  }
}
