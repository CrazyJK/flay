package jk.kamoru.flayground.service;

public class FlayException extends RuntimeException {

	/**
	 *
	 */
	public FlayException() {}

	/**
	 * @param message
	 */
	public FlayException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FlayException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FlayException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FlayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
