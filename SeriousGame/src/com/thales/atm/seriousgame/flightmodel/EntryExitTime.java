package com.thales.atm.seriousgame.flightmodel;

import java.util.Date;

//This class is use to record the entry and exit time in AirSpace
public class EntryExitTime {

	private Date entryTime;
	private Date exitTime;
	
	public EntryExitTime(Date entry,Date exit){
		entryTime = entry;
		exitTime = exit;
	}
	
	public Date getEntryTime() {
	    return entryTime;
	}
	
	public void setEntryTime(Date entry) {
	    entryTime = entry;
	}
	  
	public Date getExitTime() {
		 return exitTime;
	}
	public void setExitTime(Date exit) {
		 exitTime = exit;
	}
	
	@Override
	  public String toString() {
	    return "Keys [Entry=" + entryTime + ", Exit=" + exitTime + "]";
	  }
}
