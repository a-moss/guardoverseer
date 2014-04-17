package com.mydeblob.guard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

public class CommandHandler implements CommandExecutor, Listener{
	private final GuardOverseer plugin; 
	public CommandHandler(GuardOverseer plugin) {
		this.plugin = plugin; 
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("duty")){
			if(!(sender instanceof Player)){
				sender.sendMessage("This command can only be performed by players!");
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("guardoverseer.duty")){
				if(!onDuty(p)){
					GuardOverseer.perms.playerAdd(p, "combatlog.bypass");
					givePermissions(p);
					plugin.duty.add(p.getName());
					setPlayerFile(p);
					p.sendMessage(parseColors(plugin.getMessageConfig().getString(plugin.getConfig().getString("saved"))));
					clearInventory(p);
					giveKit(p);
					Bukkit.getServer().broadcastMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("on-duty-broadcast")).replaceAll("%p", p.getName()));
					sender.sendMessage(parseColors(plugin.getMessageConfig().getString("on-duty")));
					return true;
				}else{
					GuardOverseer.perms.playerRemove(p, "combatlog.bypass");
					removePermissions(p);
					plugin.duty.remove(p.getName());
					clearInventory(p);
					getPlayerData(p);
					unsetPlayerFile(p);
					Bukkit.getServer().broadcastMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("off-duty-broadcast")).replaceAll("%p", p.getName()));
					sender.sendMessage(parseColors(plugin.getMessageConfig().getString("off-duty")));
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
				for(String s:plugin.duty){
					String name = s + ", ";
					onduty.add(name);
				}
				p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("list-guards")).replaceAll("%g", onduty.toString()));
				return true;
			}else{
				p.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("guardoverseer")){
			if(sender.hasPermission("guardoverseer.reload")){
				plugin.reloadConfig();
				sender.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + ChatColor.GREEN + "Succesfully reloaded the configuration file!");
				return true;
			}else{
				sender.sendMessage(parseColors(plugin.getMessageConfig().getString("no-permission")));
				return true;
			}
		}
		return false;
	}
	
	public String parseColors(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	public void givePermissions(Player p){
			for(String s:plugin.getConfig().getStringList("onduty-permissions")){
				GuardOverseer.perms.playerAdd(p, s);
			}
	}
	public void removePermissions(Player p){
		for(String s:plugin.getConfig().getStringList("onduty-permissions")){
			GuardOverseer.perms.playerRemove(p, s);
		}
	}
	public void clearInventory(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setHelmet(new ItemStack(Material.AIR));
		pi.setLeggings(new ItemStack(Material.AIR));
		pi.setChestplate(new ItemStack(Material.AIR));
		pi.setBoots(new ItemStack(Material.AIR));
		p.setExp(0.0F);
		p.setGameMode(GameMode.SURVIVAL);
	}
	public boolean onDuty(Player p){
		String name = p.getName();
		if(plugin.getPlayerConfig().getConfigurationSection("playerData") != null){
			for(String k:plugin.getPlayerConfig().getConfigurationSection("playerData").getKeys(false)){
				if(k.equalsIgnoreCase(name)){
					return true;
				}
			}
		}else{
			return false;
		}
		return false;
	}
	public String getGamemode(Player p){
		if(p.getGameMode().equals(GameMode.ADVENTURE)){
			return "Adventure";
		}else if(p.getGameMode().equals(GameMode.CREATIVE)){
			return "Creative";
		}else{
			return "Survival";
		}
	}
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
			p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("error")));
		}
		for(String s:perms){
			if(p.hasPermission(s)){
				return s;
			}
		}
		p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("error")));
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
					if(getPermission(p).equalsIgnoreCase(plugin.getConfig().getString("kits." + k + ".permission"))){
						ArrayList<String> items = new ArrayList<String>();
						for(String ca: plugin.getConfig().getStringList("kits." + k + ".kit-items")){
							items.add(ca);
						}
						ArrayList<ItemStack> is = new ArrayList<ItemStack>();
						PlayerInventory pi = p.getInventory();
						for(String s:items){
							String[] sa = s.split(" "); //Break them up into spaces. This is where the plugin will break if additional spaces are provided
							int length = sa.length; //If it is over 1 than they added enchantments
							int item = 0;
							int damage = 0;
							item = parseInt(sa[0])[0];
							if(parseInt(sa[0]).length > 1){
								damage = parseInt(sa[0])[1];
							}
							ItemStack tis = new ItemStack(Material.getMaterial(item), parseIntSingle(sa[1]), (short) damage);
							if(length > 1){
								for(int i = 2;i<length;i++){
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
			p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("error")));
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){
		Player p = (Player) e.getPlayer();
		p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + ChatColor.BLUE + "Running GuardOverseer, by mydeblob");
	}
	@EventHandler
	public void onDragEvent(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(onDuty(p)){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		Player p = (Player) e.getEntity();
		if(e.getEntity() instanceof Player){
			if(!plugin.getConfig().getBoolean("drop-items-on-death")){
				if(onDuty(p)){
					e.getDrops().clear();
				}
			}
		}
	}
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if(onDuty(p)){
			if(plugin.getConfig().getBoolean("disable-hunger")){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void itemframe(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		if(e instanceof ItemFrame){
			if(onDuty(p)){
				event.setCancelled(true);
			}
		}

	}
	    
	@EventHandler
	public void playerDeathEco(PlayerDeathEvent event){
		Player player = (Player) event.getEntity();
		Player killer = (Player) event.getEntity().getKiller();
		if(plugin.getConfig().getBoolean("economy-support")){
			if(onDuty(player)){
			EconomyResponse r = GuardOverseer.econ.depositPlayer(killer.getName(), plugin.getConfig().getInt("money-given-on-death"));
			if(r.transactionSuccess()){
					killer.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("money-message")).replaceAll("%a", String.valueOf(plugin.getConfig().getInt("money-given-on-death"))));
				}
			if(!r.transactionSuccess()){
				killer.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix")) + parseColors(plugin.getMessageConfig().getString("error")));
			}
			}
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			if(event.getDamager() instanceof Player){
				Player player = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();
				if(onDuty(player) && onDuty(damager)){
					event.setCancelled(true);
					return;
				}
				if(plugin.getConfig().getBoolean("tell-guard-who-hit")){
					if(onDuty(player)){
						if(damager.getItemInHand().getType() == null){
							player.sendMessage(ChatColor.RED + damager.getName() + ChatColor.DARK_BLUE + " has hit you with his hand!");
						}
						player.sendMessage(ChatColor.DARK_RED + damager.getName() + ChatColor.DARK_BLUE + " has hit you with " + ChatColor.DARK_RED + damager.getItemInHand().getType() + ChatColor.DARK_BLUE +  "!");
					}
				}else return; //returning if the damager isn't a player
			}else return; //returning if the entity isn't a player
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent e){
		Player p = (Player) e.getPlayer();
			if(onDuty(p)){
				e.setCancelled(true);
			}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCmd(PlayerCommandPreprocessEvent e){
		Player p = (Player) e.getPlayer();
		if(onDuty(p)){
			for(String s:plugin.getConfig().getStringList("disabled-commands")){
				if(e.getMessage().startsWith(s)){
					e.setCancelled(true);
					p.sendMessage(parseColors(plugin.getMessageConfig().getString("no-command")));
				}
			}
		}
	}
	 @SuppressWarnings("static-access")
	@EventHandler
	  public void onJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();
	    if ((player.isOp()) && (plugin.UPDATE)) {
	      player.sendMessage(new StringBuilder().append(ChatColor.GREEN).append("Version ").append(plugin.NEWVERSION).append(" of GuardOverseer is up for download!").toString());
	      player.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(plugin.LINK).append(" to view the changelog and download!").toString());
	    }
	  }
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = (Player) event.getPlayer();
		if(onDuty(p)){
			giveKit(p);
		}
	}
}
