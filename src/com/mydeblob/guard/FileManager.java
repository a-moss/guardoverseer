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
	public static File langFile = null;

	public static FileManager getFileManager(){
		return instance;
	}

	public void init(Plugin p){
		this.p = p;
	}

	/*
	   --------------------Language Configuration Implementation--------------------
	 */
	public void reloadLangConfig() {
		if (langFile == null) {
			langFile = new File(p.getDataFolder(), "messages.yml");
		}
		langConfig = YamlConfiguration.loadConfiguration(langFile);
		InputStream defConfigStream = p.getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
			langConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getLangConfig() {
		if (langConfig == null) {
			this.reloadLangConfig();
		}
		return langConfig;
	}

	public void saveLangConfig() {
		if (langConfig == null || langFile == null) {
			return;
		}
		try {
			getLangConfig().save(langFile);
		} catch (IOException ex) {
			p.getLogger().log(Level.SEVERE, "Could not save config to " + langFile, ex);
		}
	}


	public void saveDefaultLangConfig() {
		if (langFile == null) {
			langFile = new File(p.getDataFolder(), "messages.yml");
		}
		if (!langFile.exists()) {            
			p.saveResource("messages.yml", false);
		}
	}
}
