package com.vaishnavs.microblogs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  private String code;
  private ErrorDetails error;

  public ErrorResponse(String code, String message) {
    this.code = code;
    this.error = new ErrorDetails(message);
  }

  public ErrorResponse(String code, String message, Object data) {
    this.code = code;
    this.error = new ErrorDetails(message, data);
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
        "code='" + code + '\'' +
        ", error=" + error.toString() +
        '}';
  }

}
