package com.xantrix.webapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Data
public class DuplicateException  extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private String messaggio;
	
	public DuplicateException(String Messaggio)
	{
		super(Messaggio);
		this.messaggio = Messaggio;
	}

}
