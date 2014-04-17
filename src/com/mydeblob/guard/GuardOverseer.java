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
import org.bukkit.scheduler.BukkitRunnable;

public class GuardOverseer extends JavaPlugin{
	FileConfiguration config;
	public ArrayList<String> duty = new ArrayList<String>();
	private FileConfiguration playerConfig = null;
	private File playerFile = null;
	private FileConfiguration messageConfig = null;
	private File messageFile = null;
	public static boolean UPDATE = false;
	public static String NEWVERSION = "";
	public static String LINK = "";
	public static Economy econ = null;
	public static Permission perms = null;
	public final Map<String, Enchantment> ENCHANTMENTS = new HashMap<String, Enchantment>();
	public void onEnable(){
		//Begining of code taken from Essentials. Let's face it, who wants to type out 100+ lines of nearly identical code?
		ENCHANTMENTS.put("alldamage", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("alldmg", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("sharpness", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("sharp", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("dal", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("dar", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("smite", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("du", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS.put("digspeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("efficiency", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("minespeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("cutspeed", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("ds", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("eff", Enchantment.DIG_SPEED);
		ENCHANTMENTS.put("durability", Enchantment.DURABILITY);
		ENCHANTMENTS.put("dura", Enchantment.DURABILITY);
		ENCHANTMENTS.put("unbreaking", Enchantment.DURABILITY);
		ENCHANTMENTS.put("d", Enchantment.DURABILITY);
		ENCHANTMENTS.put("thorns", Enchantment.THORNS);
		ENCHANTMENTS.put("highcrit", Enchantment.THORNS);
		ENCHANTMENTS.put("thorn", Enchantment.THORNS);
		ENCHANTMENTS.put("highercrit", Enchantment.THORNS);
		ENCHANTMENTS.put("t", Enchantment.THORNS);
		ENCHANTMENTS.put("fireaspect", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("fire", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("meleefire", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("meleeflame", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("fa", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS.put("knockback", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("kback", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("kb", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("k", Enchantment.KNOCKBACK);
		ENCHANTMENTS.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("looting", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("lbm", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS.put("oxygen", Enchantment.OXYGEN);
		ENCHANTMENTS.put("respiration", Enchantment.OXYGEN);
		ENCHANTMENTS.put("breathing", Enchantment.OXYGEN);
		ENCHANTMENTS.put("breath", Enchantment.OXYGEN);
		ENCHANTMENTS.put("o", Enchantment.OXYGEN);
		ENCHANTMENTS.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("bprotection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("bprotect", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS.put("fallprotection", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("fallprot", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("featherfall", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("featherfalling", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("pfa", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS.put("fireprotection", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprotection", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("fireprotect", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprotect", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("fireprot", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("flameprot", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("pf", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("projprot", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("pp", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS.put("silktouch", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("softtouch", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("st", Enchantment.SILK_TOUCH);
		ENCHANTMENTS.put("waterworker", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("aquaaffinity", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("watermine", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("ww", Enchantment.WATER_WORKER);
		ENCHANTMENTS.put("firearrow", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("flame", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("flamearrow", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("af", Enchantment.ARROW_FIRE);
		ENCHANTMENTS.put("arrowdamage", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("power", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("arrowpower", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("ad", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("punch", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("ak", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS.put("infinitearrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infarrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infinity", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("infinite", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("unlimited", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("ai", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS.put("luck", Enchantment.LUCK);
		ENCHANTMENTS.put("luckofsea", Enchantment.LUCK);
		ENCHANTMENTS.put("luckofseas", Enchantment.LUCK);
		ENCHANTMENTS.put("rodluck", Enchantment.LUCK);
		ENCHANTMENTS.put("lure", Enchantment.LURE);
		ENCHANTMENTS.put("rodlure", Enchantment.LURE);
		//End of code taken from essentials
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveDefaultConfig();
			getLogger().info("GuardOverseer --------- No config.yml found! Loading default config!");
		}
		if(!this.messageFile.exists()){
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
	//Update code token from gomeow's player vault plugin
	public void checkUpdate()
	{
		new BukkitRunnable()
		{
			public void run() {
				try {
					Updater u = new Updater(GuardOverseer.this.getDescription().getVersion());
					if ((GuardOverseer.UPDATE = u.getUpdate())) {
						GuardOverseer.LINK = u.getLink();
						GuardOverseer.NEWVERSION = u.getNewVersion();
					}
				} catch (Exception e) {
					getLogger().log(Level.WARNING, "Failed to check for updates.");
							getLogger().log(Level.WARNING, "Report this stack trace to mydeblob.");
							e.printStackTrace();
				}
			}
		}
		.runTaskAsynchronously(this);
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
			getPlayerConfig().save(messageFile);
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

}
