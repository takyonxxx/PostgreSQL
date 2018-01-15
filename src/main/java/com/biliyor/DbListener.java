package com.biliyor;

public interface DbListener {
	public void setConnection(int cmd);
	public void setValues(String id, String name, String date, String action);
}
