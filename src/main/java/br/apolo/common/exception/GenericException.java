package br.apolo.common.exception;

public class GenericException extends RuntimeException{
	
	private static final long serialVersionUID = -8428935349351960489L;
	
	private String customMsg;
	
	public GenericException() {
		super();
	}

	public GenericException(String message, Throwable cause) {
		super(message, cause);
		this.customMsg = message;
	}

	public GenericException(Throwable cause) {
		super(cause);
	}

	public String getCustomMsg() {
		return customMsg;
	}

	public void setCustomMsg(String customMsg) {
		this.customMsg = customMsg;
	}

	public GenericException(String customMsg) {
		this.customMsg = customMsg;
	}
	
}