package com.mydeblob.guard;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.mydeblob.guard.Updater.ReleaseType;

public class GuardOverseer extends JavaPlugin{
	FileConfiguration config;
//	public ArrayList<String> duty = new ArrayList<String>();
	public static Economy econ = null;
	public static Permission perms = null;
	public final Map<String, Enchantment> ENCHANTMENTS = new HashMap<String, Enchantment>();
	public final Map <String, PotionEffectType> POTIONS = new HashMap<String, PotionEffectType>();
	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";
	public static boolean vaultEnabled = true;
	public void onEnable(){
		ENCHANTMENTS.putAll(Util.initEnchantments());
		POTIONS.putAll(Util.initPotions());
		FileManager.getFileManager().init(this);
		Util.getInstance().init(this);
		GuardHandler.getInstance().init(this);
		Lang.setFile(YamlConfiguration.loadConfiguration(FileManager.langFile));
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveDefaultConfig();
			getLogger().info("[GuardOverseer] No config.yml found! Loading default config!");
		}
		File message = new File(getDataFolder(), "messages.yml");
		if(!message.exists()){
			FileManager.getFileManager().saveDefaultLangConfig();
			getLogger().info("[GuardOverseer] No messages.yml found! Loading default messages.yml!");
		}
		FileManager.getFileManager().reloadLangConfig();
		File kits = new File(getDataFolder(), "kits.yml");
		if(!kits.exists()){
			getLogger().info("[GuardOverseer] No kits.yml found! Loading defualt kits.yml!");
		}
		FileManager.getFileManager().reloadKitsConfig();
		File data = new File(getDataFolder(), "data.yml");
		if(!data.exists()){
			getLogger().info("[GuardOverseer] No data.yml found! Loading defualt kits.yml!");
		}
		FileManager.getFileManager().reloadDataConfig();
		if (!setupEconomy()) {
			getLogger().log(Level.SEVERE, "[GuardOverseer] Vault is not installed! Some features will not work properly! Please install it to get 100% functionality.");
			vaultEnabled = false;
		}
		if(vaultEnabled){
			setupPermissions();
		}
		getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gupdate").setExecutor(new CommandHandler(this));
		getCommand("greload").setExecutor(new CommandHandler(this));
		getCommand("duty").setExecutor(new CommandHandler(this));
		getCommand("guards").setExecutor(new CommandHandler(this));
		if(getConfig().getBoolean("auto-updater")){
			Updater updater = new Updater(this, 66080, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
			name = updater.getLatestName(); // Get the latest name
			version = updater.getLatestGameVersion(); // Get the latest game version
			type = updater.getLatestType(); // Get the latest file's type
			link = updater.getLatestFileLink(); // Get the latest link
		}
		getLogger().info("[GuardOverseer] Successfully enabled."); 
//		reloadPlayerConfig();
//		if(getPlayerConfig().getConfigurationSection("playerData") == null){
//			getPlayerConfig().createSection("playerData");
//		}
//		if(getPlayerConfig().getConfigurationSection("onDuty") != null){
//			for(String k:getPlayerConfig().getStringList("onDuty")){
//				duty.add(k);
//			}
//		}
//		savePlayerConfig();
	}

	public void onDisable(){
		getLogger().info("The Guard Plugin has been disabled, made by mydeblob");
//		getPlayerConfig().set("onDuty", Arrays.asList(duty));
//		savePlayerConfig();
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

}
