package com.github.sirkarpfen.exceptions;

public class IllegalSpawnException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalSpawnException() {
		super();
	}

	public IllegalSpawnException(String message) {
		super(message);
	}

	public IllegalSpawnException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalSpawnException(Throwable cause) {
		super(cause);
	}

}
