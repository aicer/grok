package org.aicer.grok.exception;

public class GrokException extends RuntimeException {

  /**
   * Serial Version ID for the class
   */
  private static final long serialVersionUID = -1138511096189746111L;

  public GrokException() {
    super();
  }

  public GrokException(String message) {
    super(message);
  }

  public GrokException(Throwable cause) {
    super(cause);
  }

  public GrokException(String message, Throwable cause) {
    super(message, cause);
  }

  public GrokException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
