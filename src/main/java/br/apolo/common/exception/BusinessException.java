package br.apolo.common.exception;

public class BusinessException extends GenericException {

	private static final long serialVersionUID = 6102557498006358663L;
	
	public BusinessException(String customMsg) {
		super(customMsg);
	}
	
}