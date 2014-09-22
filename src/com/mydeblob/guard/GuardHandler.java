package com.mydeblob.guard;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GuardHandler {
	static GuardHandler instance = new GuardHandler();
	private ArrayList<String> onDuty = new ArrayList<String>();
	
	public static GuardHandler getInstance(){
		return instance;
	}

	public ArrayList<String> getDuty(){
		return onDuty;
	}
	
	public boolean onDuty(Player p){
		String name = p.getName();
		return onDuty.contains(name);
	}
	
	public void setOnDuty(Player p){
		
	}
	
	public void setOffDuty(Player p){
		
	}
}
