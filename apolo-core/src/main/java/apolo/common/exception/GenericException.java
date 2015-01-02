package apolo.common.exception;

public class GenericException extends RuntimeException{
	
	private static final long serialVersionUID = -8428935349351960489L;
	
	private Integer principalCode;
	private Integer secondCode;
	
	private String customMsg;
	
	public GenericException() {
		super();
	}
	
	public GenericException(String customMsg) {
		this.customMsg = customMsg;
	}
	
	public GenericException(Integer principalCode, String customMsg) {
		this.principalCode = principalCode;
		this.customMsg = customMsg;
	}
	
	public GenericException(Integer principalCode, Integer secondCode, String customMsg) {
		this.principalCode = principalCode;
		this.secondCode = secondCode;
		this.customMsg = customMsg;
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

	public Integer getPrincipalCode() {
		return this.principalCode;
	}

	public void setPrincipalCode(Integer principalCode) {
		this.principalCode = principalCode;
	}

	public Integer getSecondCode() {
		return this.secondCode;
	}

	public void setSecondCode(Integer secondCode) {
		this.secondCode = secondCode;
	}
	
}