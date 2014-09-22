package com.mydeblob.guard;

import org.bukkit.ChatColor;
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

public class Events implements Listener{

	GuardHandler gh = GuardHandler.getInstance();
	@EventHandler
	public void onDragEvent(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(gh.onDuty(p)){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		Player p = (Player) e.getEntity();
		if(e.getEntity() instanceof Player){
			if(!plugin.getConfig().getBoolean("drop-items-on-death")){
				if(gh.onDuty(p)){
					e.getDrops().clear();
				}
			}
		}
	}
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if(gh.onDuty(p)){
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
			if(gh.onDuty(p)){
				event.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void playerDeathEco(PlayerDeathEvent event){
		Player player = (Player) event.getEntity();
		if(event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			if(plugin.getConfig().getBoolean("give-money")){
				if(!killer.getName().equalsIgnoreCase(player.getName())){
					if(onDuty(player)){
						GuardOverseer.econ.depositPlayer(killer.getName(), plugin.getConfig().getInt("amount"));
						killer.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + parseColors(plugin.getMessageConfig().getString("money-message").replaceAll("%a%", String.valueOf(plugin.getConfig().getInt("amount")))));
					}
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
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e){
		Player p = (Player) e.getPlayer();
		if(gh.onDuty(p)){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e){
		Player p = (Player) e.getPlayer();
		if(gh.onDuty(p)){
			for(String s:plugin.getConfig().getStringList("disabled-commands")){
				if(e.getMessage().startsWith(s)){
					e.setCancelled(true);
					p.sendMessage(parseColors(plugin.getMessageConfig().getString("no-command")));
				}
			}
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = (Player) event.getPlayer();
		if(gh.onDuty(p)){
			giveKit(p);
		}
	}
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){
		Player p = (Player) e.getPlayer();
		p.sendMessage(parseColors(plugin.getMessageConfig().getString("prefix"))  + " " + ChatColor.BLUE + "Running GuardOverseer, by mydeblob");
		if(p.hasPermission("guardoverseer.update") && GuardOverseer.update && plugin.getConfig().getBoolean("auto-updater")){
			p.sendMessage(ChatColor.BLUE + "An update is available: " + GuardOverseer.name + ", a " + GuardOverseer.type + " for " + GuardOverseer.version + "; Available at " + GuardOverseer.link);
			// Will look like - An update is available: AntiCheat v1.5.9, a release for CB 1.6.2-R0.1 available at http://media.curseforge.com/XYZ
			p.sendMessage(ChatColor.BLUE + "Type /gupdate if you would like to automatically update.");
		}
	}
}
