package com.mydeblob.guard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
	private Plugin p;
	static FileManager instance = new FileManager();
	private static FileConfiguration langConfig = null;
	public static File langFile = null; //This needs to be public so we can init the lang class
	private static FileConfiguration kitsConfig = null;
	private static File kitsFile = null;
	private static FileConfiguration dataConfig = null;
	private static File dataFile = null;

	public static FileManager getFileManager(){
		return instance;
	}

	public void init(Plugin p){
		this.p = p;
	}

	//TODO Test this
	/*
	   --------------------Data Configuration--------------------
	 */
	public void reloadDataConfig(){
		if(dataFile == null){
			dataFile = new File(p.getDataFolder(), "data.yml");
		}
		dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		InputStream defConfigStream = p.getResource("data.yml");
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(dataFile);
			dataConfig.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getDataConfig(){
		if(dataConfig == null){
			reloadDataConfig();
		}
		return dataConfig;
	}
	
	public void saveDataConfig(){
		if(dataConfig == null || dataFile == null){
			return;
		}
		try{
			getDataConfig().save(dataFile);
		}catch(IOException e){
			p.getLogger().log(Level.SEVERE, "[GuardOverseer] Failed to save the data.yml! Error:");
			e.printStackTrace();
		}
	}
	
	/*
	   --------------------Kits Configuration--------------------
	 */
	public void reloadKitsConfig(){
		if(kitsFile == null){
			kitsFile = new File(p.getDataFolder(), "kits.yml");
		}
		kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
		InputStream defConfigStream = p.getResource("kits.yml");
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(kitsFile);
			kitsConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getKitsConfig(){
		if(kitsConfig == null){
			reloadKitsConfig();
		}
		return kitsConfig;
	}

	public void saveKitsConfig(){
		if(kitsConfig == null || kitsFile == null){
			return;
		}
		try{
			getKitsConfig().save(kitsFile);
		}catch(IOException e){
			p.getLogger().log(Level.SEVERE, "[GuardOverseer] Failed to save the kits.yml! Error:");
			e.printStackTrace();
		}
	}
	
	public void saveDefaultKitsConfigAndOverwrite(){
		if(kitsFile == null){
			kitsFile = new File(p.getDataFolder(), "kits.yml");
		}         
		if(kitsFile.exists()){
			kitsFile.delete();
		}
		p.saveResource("kits.yml", false);
		saveKitsConfig();
	}

	/*
	   --------------------Language Configuration--------------------
	 */	
	public void reloadLangConfig(){
		if(langFile == null){
			langFile = new File(p.getDataFolder(), "messages.yml");
		}
		langConfig = YamlConfiguration.loadConfiguration(langFile);
		InputStream defConfigStream = p.getResource("messages.yml");
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
			langConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getLangConfig(){
		if(langConfig == null){
			this.reloadLangConfig();
		}
		return langConfig;
	}

	public void saveLangConfig() {
		if(langConfig == null || langFile == null){
			return;
		}
		try{
			getLangConfig().save(langFile);
		}catch (IOException e){
			p.getLogger().log(Level.SEVERE, "[GuardOverseer] Failed to save the messages.yml! Error:");
			e.printStackTrace();
		}
	}


	public void saveDefaultLangConfig(){
		if(langFile == null){
			langFile = new File(p.getDataFolder(), "messages.yml");
		}
		if(!langFile.exists()){            
			p.saveResource("messages.yml", false);
		}
	}
}
