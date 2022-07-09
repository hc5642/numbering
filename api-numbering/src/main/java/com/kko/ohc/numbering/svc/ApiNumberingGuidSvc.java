package com.kko.ohc.numbering.svc;

public interface ApiNumberingGuidSvc {
	
	public String createGuid();
	
	public String nextGuid(String guid);

}
