package com.mydeblob.guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandHandler implements CommandExecutor, Listener{
	private final GuardOverseer plugin; 
	GuardHandler gh = GuardHandler.getInstance();
	public CommandHandler(GuardOverseer plugin) {
		this.plugin = plugin; 
	}

	//	private ArrayList<String> onDuty = new ArrayList<String>();
	//	private HashMap<String, Long> timeDuty = new HashMap<String, Long>();
	//	private HashMap<String, Location> afk = new HashMap<String, Location>();
	//	private HashMap<String, Integer> strikes = new HashMap<String, Integer>();
	//	private HashMap<String, Integer> afkPay = new HashMap<String, Integer>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("duty")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "This command can only be performed by players!");
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("guardoverseer.duty")){
				if(!gh.onDuty(p)){
					gh.setOnDuty(p);
					GuardOverseer.perms.playerAdd(p, "combatlog.bypass");
					//					givePermissions(p);
					//					givePotions(p);
					//					plugin.duty.add(p.getName());
					//					setPlayerFile(p);
					//					p.sendMessage(parseColors(plugin.getMessageConfig().getString("saved")));
					//					clearInventory(p);
					//					giveKit(p);
					//					startTime(p);
					if(Lang.ON_DUTY_BROADCAST.send()){
						Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.ON_DUTY_BROADCAST.toString(Arrays.asList("%p%"), Arrays.asList(p.getName())));
					}
					if(Lang.ON_DUTY.send()){
						p.sendMessage(Lang.PREFIX.toString() + Lang.ON_DUTY.toString(Arrays.asList("%p%"), Arrays.asList(p.getName())));
					}
					return true;
				}else{
					GuardOverseer.perms.playerRemove(p, "combatlog.bypass");
					removePermissions(p);
					removePotions(p);
					plugin.duty.remove(p.getName());
					clearInventory(p);
					getPlayerData(p);
					unsetPlayerFile(p);
					Bukkit.getServer().broadcastMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("off-duty-broadcast")).replaceAll("%p", p.getName()));
					sender.sendMessage(parseColors(plugin.getMessageConfig().getString("off-duty")));
					if(afkPay.containsKey(p.getName())){
						endTime(p, afkPay.get(p.getName()));
					}else{
						endTime(p, 0);
					}
					return true;
				}
			}else{
				p.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("guards")){
			if(!(sender instanceof Player)){
				sender.sendMessage("This command can only be performed by players!");
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("guardoverseer.guards")){
				ArrayList<String> onduty = new ArrayList<String>();
				onduty.clear();
				for(String s:plugin.duty){
					String name = s + ", ";
					onduty.add(name);
				}
				p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("list-guards")).replaceAll("%g", onduty.toString()));
				return true;
			}else{
				p.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("gupdate")){
			if(sender.hasPermission("guardoverseer.update")){
				if(plugin.getConfig().getBoolean("auto-updater")){
					@SuppressWarnings("unused")
					Updater updater = new Updater(plugin, 66080, plugin.getF(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
					sender.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + " " + ChatColor.GREEN + "Starting the download of the latest version of GuardOverseer. Check console for progress on the download. Reload after is has downloaded!");
					return true;
				}else{
					sender.sendMessage(ChatColor.RED + "Please enable auto updating in the GuardOverseer config.yml to use this feature");
					return true;
				}
			}else{
				sender.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("greload")){
			if(sender.hasPermission("guardoverseer.reload")){
				if(sender instanceof ConsoleCommandSender){
					Server server = Bukkit.getServer();
					ConsoleCommandSender c = server.getConsoleSender();
					plugin.reloadConfig();
					plugin.reloadMessageConfig();
					plugin.saveConfig();
					plugin.saveMessageConfig();
					c.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + " " + ChatColor.GREEN + "Successfully reloaded the configurations for GuardOverseer!");
					return true;
				}else if(sender instanceof Player){
					Player p = (Player) sender;
					plugin.reloadConfig();
					plugin.reloadMessageConfig();
					plugin.saveConfig();
					plugin.saveMessageConfig();
					p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + " " + ChatColor.GREEN + "Successfully reloaded the configurations for GuardOverseer!");
					return true;
				}
			}else{
				sender.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		return false;
	}

	//	public void startTime(Player p){
	//		if(timeDuty.containsKey(p.getName())){
	//			timeDuty.remove(p.getName());
	//		}
	//		if(!plugin.getConfig().getBoolean("pay-guards")) return;
	//		timeDuty.put(p.getName(), System.currentTimeMillis());
	//		if(plugin.getConfig().getBoolean("afk")){
	//			afk.put(p.getName(), p.getLocation());
	//			strikes.put(p.getName(), 0);
	//		}
	//	}
	//	public void pauseTime(Player p){
	//		if(!timeDuty.containsKey(p.getName())){
	//			return;
	//		}
	//		if(!plugin.getConfig().getBoolean("pay-guards")) return;
	//		long start = timeDuty.get(p.getName());
	//		int seconds = (int)TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - start)); //in seconds
	//		int minutes = (int) seconds/60;
	//		int pay = (plugin.getConfig().getInt("salary")/60)*minutes;
	//		if(afkPay.containsKey(p.getName())){
	//			afkPay.put(p.getName(), afkPay.get(p.getName())+pay);
	//		}else{
	//			afkPay.put(p.getName(), pay);
	//		}
	//	}
	//	public void endTime(Player p, int additional){
	//		if(!timeDuty.containsKey(p.getName())){
	//			return;
	//		}
	//		if(!plugin.getConfig().getBoolean("pay-guards")) return;
	//		long start = timeDuty.get(p.getName());
	//		int seconds = (int)TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - start)); //in seconds
	//		int minutes = (int) seconds/60;
	//		int pay = (plugin.getConfig().getInt("salary")/60);
	//		GuardOverseer.econ.depositPlayer(p.getName(), pay*minutes+additional);
	//		p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + " " + parseColors(plugin.getMessageConfig().getString("pay-day")).replaceAll("%a%", String.valueOf(pay*minutes)).replaceAll("%t%", String.valueOf(minutes)));
	//	}

	public void setPlayerFile(Player p){
		PlayerInventory pi = p.getInventory();
		plugin.getPlayerConfig().set("playerData", p.getName());
		plugin.getPlayerConfig().set("playerData." + p.getName() + ".gamemode", getGamemode(p));
		plugin.getPlayerConfig().set("playerData." + p.getName() + ".xp", p.getExp());
		plugin.getPlayerConfig().set("playerData." + p.getName() + ".inventory", pi.getContents());
		plugin.getPlayerConfig().set("playerData." + p.getName() + ".armor", pi.getArmorContents());
		plugin.savePlayerConfig();
	}
	public void unsetPlayerFile(Player p){
		plugin.getPlayerConfig().set("playerData." + p.getName(), null);
		plugin.savePlayerConfig();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getPlayerData(Player p){
		Object a = plugin.getPlayerConfig().get("playerData." + p.getName() + ".inventory");
		Object b = plugin.getPlayerConfig().get("playerData." + p.getName() + ".armor");
		if(a == null || b == null){
			p.sendMessage(parseColors(plugin.getMessageConfig().getString("fail-loaded")));
			return;
		}
		ItemStack[] inventory = null;
		ItemStack[] armor = null;
		if (a instanceof ItemStack[]){
			inventory = (ItemStack[]) a;
		} else if (a instanceof List){
			List lista = (List) a;
			inventory = (ItemStack[]) lista.toArray(new ItemStack[0]);
		}
		if (b instanceof ItemStack[]){
			armor = (ItemStack[]) b;
		} else if (b instanceof List){
			List listb = (List) b;
			armor = (ItemStack[]) listb.toArray(new ItemStack[0]);
		}
		p.getInventory().setContents(inventory);
		p.getInventory().setArmorContents(armor);
		if(plugin.getPlayerConfig().getString("playerData." + p.getName() + ".gamemode").equalsIgnoreCase("Survival")){
			p.setGameMode(GameMode.SURVIVAL);
		}else if(plugin.getPlayerConfig().getString("playerData." + p.getName() + ".gamemode").equalsIgnoreCase("Creative")){
			p.setGameMode(GameMode.CREATIVE);
		}else{
			p.setGameMode(GameMode.ADVENTURE);
		}
		p.setExp(plugin.getPlayerConfig().getLong("playerData." + p.getName() + ".xp"));
		plugin.savePlayerConfig();
		p.sendMessage(parseColors(plugin.getMessageConfig().getString("succesfully-loaded")));
	}
	public String getPermission(Player p){
		List<String> perms = new ArrayList<String>();
		if(plugin.getConfig().getConfigurationSection("kits") != null){
			for(String k:plugin.getConfig().getConfigurationSection("kits").getKeys(false)){
				perms.add(plugin.getConfig().getString("kits." + k + ".permission"));
			}
		}else{
			p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("error")));
		}
		for(String s:perms){
			if(p.hasPermission(s)){
				return s;
			}
		}
		p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("error")));
		return null;
	}
	@SuppressWarnings("deprecation")
	public boolean isHelmet(int helmetId){
		if(helmetId == Material.DIAMOND_HELMET.getId()){
			return true;
		}else if(helmetId == Material.IRON_HELMET.getId()){
			return true;
		}else if(helmetId == Material.CHAINMAIL_HELMET.getId()){
			return true;
		}else if(helmetId == Material.GOLD_HELMET.getId()){
			return true;
		}else if(helmetId == Material.LEATHER_HELMET.getId()){
			return true;
		}else{
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public boolean isChestplate(int chestplateId){
		if(chestplateId == Material.DIAMOND_CHESTPLATE.getId()){
			return true;
		}else if(chestplateId == Material.IRON_CHESTPLATE.getId()){
			return true;
		}else if(chestplateId == Material.CHAINMAIL_CHESTPLATE.getId()){
			return true;
		}else if(chestplateId == Material.GOLD_CHESTPLATE.getId()){
			return true;
		}else if(chestplateId == Material.LEATHER_CHESTPLATE.getId()){
			return true;
		}else{
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public boolean isLeggings(int legId){
		if(legId == Material.DIAMOND_LEGGINGS.getId()){
			return true;
		}else if(legId == Material.IRON_LEGGINGS.getId()){
			return true;
		}else if(legId == Material.CHAINMAIL_LEGGINGS.getId()){
			return true;
		}else if(legId == Material.GOLD_LEGGINGS.getId()){
			return true;
		}else if(legId == Material.LEATHER_LEGGINGS.getId()){
			return true;
		}else{
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public boolean isBoots(int bootsId){
		if(bootsId == Material.DIAMOND_BOOTS.getId()){
			return true;
		}else if(bootsId == Material.IRON_BOOTS.getId()){
			return true;
		}else if(bootsId == Material.CHAINMAIL_BOOTS.getId()){
			return true;
		}else if(bootsId == Material.GOLD_BOOTS.getId()){
			return true;
		}else if(bootsId == Material.LEATHER_BOOTS.getId()){
			return true;
		}else{
			return false;
		}
	}
	public int[] parseInt(String stringToParse){
		try{
			if(stringToParse.contains(":")){
				String[] toSplit = stringToParse.split(":");
				int[] returning = new int[2];
				returning[0] = Integer.parseInt(toSplit[0]);
				returning[1] = Integer.parseInt(toSplit[1]);
				return returning;
			}else{
				int[] returning = new int[1];
				returning[0] = Integer.parseInt(stringToParse);
				return returning;
			}
		}catch(NumberFormatException e){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "---------------PLEASE REPORT THIS TO MYDEBLOB, THE GUARDOVERSEER DEVELOPER---------------");
			e.printStackTrace();
		}
		int[] returning = null;
		return returning;
	}
	public int parseIntSingle(String stringToParse){
		try{
			return Integer.parseInt(stringToParse);
		}catch(NumberFormatException e){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "---------------PLEASE REPORT THIS TO MYDEBLOB, THE GUARDOVERSEER DEVELOPER---------------");
			e.printStackTrace();
		}
		return 0;
	}
	@SuppressWarnings({"deprecation"})
	public void giveKit(Player p){
		if(plugin.getConfig().getConfigurationSection("kits") != null){
			for(String k: plugin.getConfig().getConfigurationSection("kits").getKeys(false)){
				if(getPermission(p) != null & getPermission(p).equalsIgnoreCase(plugin.getConfig().getString("kits." + k + ".permission"))){
					ArrayList<String> items = new ArrayList<String>();
					for(String ca: plugin.getConfig().getStringList("kits." + k + ".kit-items")){
						items.add(ca);
					}
					ArrayList<ItemStack> is = new ArrayList<ItemStack>();
					PlayerInventory pi = p.getInventory();
					for(String s:items){
						String[] sa = s.split(" "); //Break them up into spaces. This is where the plugin will break if additional spaces are provided
						int length = sa.length; //If it is over 3 than they added enchantments
						int item = 0;
						int damage = 0;
						item = parseInt(sa[0])[0];
						if(parseInt(sa[0]).length > 1){
							damage = parseInt(sa[0])[1];
						}
						ItemStack tis = new ItemStack(Material.getMaterial(item), parseIntSingle(sa[1]), (short) damage);
						ItemMeta im = tis.getItemMeta();
						if(sa.length > 3){
							String[] name = sa[2].split(":");
							if(!name[1].equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', "null")))){
								im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name[1].replaceAll("_", " ")));
							}
							String[] lores = sa[3].split(":");
							if(!lores[1].equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', "null")))){
								ArrayList<String> lore = new ArrayList<String>();
								for(String ss:lores[1].split("%n%")){
									lore.add(ChatColor.translateAlternateColorCodes('&', ss.replaceAll("_", " ")));
								}
								im.setLore(lore);
							}
							tis.setItemMeta(im);
						}
						if(length > 3){
							for(int i = 3;i<length;i++){
								String[] el = sa[i].split(":");
								String e = el[0];
								String l = el[1];
								if(plugin.ENCHANTMENTS.containsKey(e)){
									tis.addUnsafeEnchantment(plugin.ENCHANTMENTS.get(e), parseIntSingle(l));
								}
							}
						}
						is.add(tis); 
					}
					for(ItemStack iss:is){
						if(isHelmet(iss.getTypeId())){
							pi.setHelmet(iss);
							continue;
						}if(isChestplate(iss.getTypeId())){
							pi.setChestplate(iss);
							continue;
						}if(isLeggings(iss.getTypeId())){
							pi.setLeggings(iss);
							continue;
						}if(isBoots(iss.getTypeId())){
							pi.setBoots(iss);
							continue;
						}
						pi.addItem(iss);
						continue;
					}
				} 
			}
		}else{
			p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("error")));
		}
	}

	public void afk(){
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new BukkitRunnable(){
			public void run(){
				for(Player p:Bukkit.getOnlinePlayers()){
					if(onDuty(p)){
						if(!plugin.getConfig().getBoolean("afk")) return;
						if(afk.containsKey(p.getName())){
							Location afkl = afk.get(p.getName());
							if(afkl.getX() == p.getLocation().getX() && afkl.getY() == p.getLocation().getY() && afkl.getZ() == p.getLocation().getZ()){
								if(strikes.containsKey(p.getName())){
									if(strikes.get(p.getName()) == 3){
										pauseTime(p);
									}else{
										strikes.put(p.getName(), strikes.get(p.getName()) + 1);
									}
								}else{
									strikes.put(p.getName(), 1);
								}
							}
						}
					}
				}
			}
		}, 0L, 20*60L);
	}

}
