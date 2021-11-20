package jk.kamoru.flayground.source;

import jk.kamoru.flayground.service.FlayException;

public class FlayNotfoundException extends FlayException {

	/**
	 *
	 */
	public FlayNotfoundException() {}

	/**
	 * @param message
	 */
	public FlayNotfoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FlayNotfoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FlayNotfoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FlayNotfoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
