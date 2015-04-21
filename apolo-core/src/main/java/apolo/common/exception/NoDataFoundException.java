package apolo.common.exception;

public class NoDataFoundException extends GenericException {

	private static final long serialVersionUID = 6102557498006358663L;
	
	public NoDataFoundException(String customMsg) {
		super(customMsg);
	}
	
	public NoDataFoundException(int principalCode, String customMsg) {
		super(principalCode, customMsg);
	}
	
	public NoDataFoundException(int principalCode, int secondCode, String customMsg) {
		super(principalCode, secondCode, customMsg);
	}
	
}