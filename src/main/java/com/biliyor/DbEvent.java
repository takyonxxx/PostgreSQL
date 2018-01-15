package com.biliyor;

import java.util.EventObject;

public class DbEvent extends EventObject {
	
	private String id;
	private String name;
	private String sdate;
	private String action;
	

	public DbEvent(Object source, String id, String name,String sdate,String action) {
		super(source);
		
		this.id = id;
		this.name = name;
		this.sdate = sdate;
		this.action = action;		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSdate() {
		return sdate;
	}


	public void setSdate(String sdate) {
		this.sdate = sdate;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

}
