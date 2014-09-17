package com.mydeblob.guard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.mydeblob.guard.Updater.ReleaseType;

public class GuardOverseer extends JavaPlugin{
	FileConfiguration config;
	public ArrayList<String> duty = new ArrayList<String>();
	private FileConfiguration playerConfig = null;
	private File playerFile = null;
	private FileConfiguration messageConfig = null;
	private File messageFile = null;
	public static Economy econ = null;
	public static Permission perms = null;
	public final Map<String, Enchantment> ENCHANTMENTS = new HashMap<String, Enchantment>();
	public final Map <String, PotionEffectType> POTIONS = new HashMap<String, PotionEffectType>();
	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";
	public void onEnable(){
		PotEnchInit.initEnchantments();
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveDefaultConfig();
			getLogger().info("GuardOverseer --------- No config.yml found! Loading default config!");
		}
		File message = new File(getDataFolder(), "messages.yml");
		if(!message.exists()){
			this.saveDefaultMessageConfig();
			getLogger().info("GuardOverseer --------- No messages.yml found! Loading default messages.yml!");
		}
		if (!setupEconomy() ) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		//Printing to the console
		getLogger().info("The Guard Overseer Plugin has been enabled, made by mydeblob");
		getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gupdate").setExecutor(new CommandHandler(this));
		getCommand("greload").setExecutor(new CommandHandler(this));
		getCommand("duty").setExecutor(new CommandHandler(this));
		getCommand("guards").setExecutor(new CommandHandler(this)); 
		setupPermissions();
		reloadPlayerConfig();
		if(getPlayerConfig().getConfigurationSection("playerData") == null){
			getPlayerConfig().createSection("playerData");
		}
		if(getPlayerConfig().getConfigurationSection("onDuty") != null){
			for(String k:getPlayerConfig().getStringList("onDuty")){
				duty.add(k);
			}
		}
		savePlayerConfig();
		if(getConfig().getBoolean("auto-updater")){
			Updater updater = new Updater(this, 66080, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
			name = updater.getLatestName(); // Get the latest name
			version = updater.getLatestGameVersion(); // Get the latest game version
			type = updater.getLatestType(); // Get the latest file's type
			link = updater.getLatestFileLink(); // Get the latest link
		}
	}

	public void onDisable(){
		getLogger().info("The Guard Plugin has been disabled, made by mydeblob");
		getPlayerConfig().set("onDuty", Arrays.asList(duty));
		savePlayerConfig();
	}
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public void reloadPlayerConfig() {
		if (playerFile == null) {
			playerFile = new File(getDataFolder(), "playerData.yml");
		}
		playerConfig = YamlConfiguration.loadConfiguration(playerFile);

		InputStream defConfigStream = this.getResource("playerData.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			playerConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getPlayerConfig() {
		if (playerConfig == null) {
			reloadPlayerConfig();
		}
		return playerConfig;
	}

	public void savePlayerConfig() {
		if (playerConfig == null || playerFile == null) {
			return;
		}
		try {
			getPlayerConfig().save(playerFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + playerFile, ex);
		}
	}

	public void reloadMessageConfig() {
		if (messageFile == null) {
			messageFile = new File(getDataFolder(), "messages.yml");
		}
		messageConfig = YamlConfiguration.loadConfiguration(messageFile);

		InputStream defConfigStream = this.getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messageConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMessageConfig() {
		if (messageConfig == null) {
			reloadMessageConfig();
		}
		return messageConfig;
	}

	public void saveMessageConfig() {
		if (messageConfig == null || messageFile == null) {
			return;
		}
		try {
			getMessageConfig().save(messageFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + messageFile, ex);
		}
	}
	public void saveDefaultMessageConfig() {
	    if (messageFile == null) {
	        messageFile = new File(getDataFolder(), "messages.yml");
	    }
	    if (!messageFile.exists()) {            
	         this.saveResource("messages.yml", false);
	     }
	}
	public File getF(){
		return this.getFile();
	}
}
