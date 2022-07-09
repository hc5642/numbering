package com.kko.ohc.numbering.svc;

public interface ApiNumberingGuidSvc {
	
	public String createGuid(String hostname);
	
	public String nextGuid(String guid, String hostname);
	
}
