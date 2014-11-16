package com.mydeblob.guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class GuardHandler {
	static GuardHandler instance = new GuardHandler();
	private ArrayList<String> onDuty = new ArrayList<String>();
	
	private static FileManager f = FileManager.getFileManager();
	
	private GuardOverseer p;
	public void init(GuardOverseer p){
		this.p = p;
	}
	private static Util u = Util.getInstance();

	public static GuardHandler getInstance(){
		return instance;
	}

	public ArrayList<String> getDuty(){
		return onDuty;
	}

	public boolean onDuty(Player p){
		return onDuty.contains(p.getName());
	}

	public void setOnDuty(Player p){
		u.givePermissions(p, this.p.getConfig().getStringList("permissions"));
	}

	public void setOffDuty(Player p){

	}
	
	public String getPermission(Player p){
		List<String> perms = new ArrayList<String>();
		if(f.getKitsConfig().getConfigurationSection("kits") == null){
			f.saveDefaultKitsConfigAndOverwrite();
		}
		for(String key:f.getKitsConfig().getConfigurationSection("kits").getKeys(false)){
			perms.add(f.getKitsConfig().getString("kits." + key + ".permission"));
		}
		for(String s:perms){
			if(p.hasPermission(s)){
				return s;
			}
		}
		return null;
	}
	
	public HashMap<String, Integer> getPotionsAndLevels(Player p){
		if(f.getKitsConfig().getConfigurationSection("kits") != null){
			for(String key:f.getKitsConfig().getConfigurationSection("kits").getKeys(false)){
				if(getPermission(p).equalsIgnoreCase(f.getKitsConfig().getString("kits." + key + ".permission"))){
					
				}
			}
		}
		if(plugin.getConfig().getConfigurationSection("kits") != null){
			for(String k: plugin.getConfig().getConfigurationSection("kits").getKeys(false)){
					if(getPermission(p) != null && getPermission(p).equalsIgnoreCase(plugin.getConfig().getString("kits." + k + ".permission"))){
									ArrayList<String> potionlist = new ArrayList<String>();
									for(String s:plugin.getConfig().getStringList("kits." + k + ".potions")){
										potionlist.add(s);
									}
									for(String s:potionlist){
										String[] pe = s.split(" ");
										if(plugin.POTIONS.containsKey(pe[0])){
											p.addPotionEffect(new PotionEffect(plugin.POTIONS.get(pe[0]), max_value, (parseIntSingle(pe[1])-1)));
										}
									}
								}
							}
						}
	}


}
