package com.cfdi.exceptions;

public class myXmlParsingException extends Throwable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1722274377497080399L;

	
    public myXmlParsingException(Throwable t){
        super(t.getMessage());
    }

}
