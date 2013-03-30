package com.github.sirkarpfen.exceptions;

/**
 * Thrown when no spawn was detected by the player. This only happens, if
 * a map has no object with the name: "spawn" declared.
 * 
 * @author sirkarpfen
 *
 */
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
