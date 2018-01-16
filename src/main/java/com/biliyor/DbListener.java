package com.biliyor;

import java.io.File;

public interface DbListener {
	public void setConnection(int cmd);
	public void setValues(String id, String name, String imageName, byte[] imageBytes ,String date, String action);
}
