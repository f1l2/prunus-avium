package at.f1l2.prunus.avium.core.exception;

public class AviumCoreException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AviumCoreException(String message) {
		super(message);
	}
	
	public AviumCoreException(String message, Exception e) {
		super(message, e);
	}
}
