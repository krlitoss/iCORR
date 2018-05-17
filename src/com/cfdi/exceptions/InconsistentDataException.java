package com.cfdi.exceptions;

import java.io.Serializable;

public class InconsistentDataException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public InconsistentDataException(String msg){
        super(msg);
    }
    
    public InconsistentDataException(Exception e){
        super(e.getMessage());
    }

    public InconsistentDataException(){
    	
    }
}
