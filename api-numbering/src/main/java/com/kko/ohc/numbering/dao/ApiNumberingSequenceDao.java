package com.kko.ohc.numbering.dao;

public interface ApiNumberingSequenceDao {
	
	public double select();
	
	public double getNext();
	
	public int initTable();

}
