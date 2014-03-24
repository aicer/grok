package org.aicer.grok.exception;

public class GrokCompilationException extends GrokException {

  private static final long serialVersionUID = -7178169318219517559L;

  public GrokCompilationException() {
    super();
  }

  public GrokCompilationException(String message) {
    super(message);
  }

  public GrokCompilationException(Throwable cause) {
    super(cause);
  }

  public GrokCompilationException(String message, Throwable cause) {
    super(message, cause);
  }

  public GrokCompilationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
