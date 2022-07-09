package com.kko.ohc.numbering.dao;

public interface ApiNumberingGuidDao {
	
	public int insert(String guid);
	
	public int getNext(String guid);
	
	public int initTable();

}
