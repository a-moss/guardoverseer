package com.mydeblob.guard;

import java.util.Arrays;

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

	private static GuardHandler gh = GuardHandler.getInstance();
	private GuardOverseer p;
	public void init(GuardOverseer p){
		this.p = p;
	}
	
	/**
	 * This disables guards from dropping items
	 * 
	 * @bug - Although this prevents them from dropping items they also can't move items in their inventory
	 */
	@EventHandler
	public void onDragEvent(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(gh.onDuty(p)){
			e.setCancelled(true);
		}
	}
	
	/**
	 * This prevents items from dropping on death if enabled
	 */
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		Player p = (Player) e.getEntity();
		if(e.getEntity() instanceof Player){
			if(!this.p.getConfig().getBoolean("drop-items-on-death")){
				if(gh.onDuty(p)){
					e.getDrops().clear();
				}
			}
		}
	}
	
	/**
	 * Disables hunger loss while a guard is on duty if the option is enabled
	 */
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if(gh.onDuty(p)){
			if(!this.p.getConfig().getBoolean("hunger")){
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Prevents a exploit where guards can put items in itemframes
	 */
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

	/**
	 * Awards people for killing guards
	 */
	@EventHandler
	public void playerDeathEco(PlayerDeathEvent event){
		Player player = (Player) event.getEntity();
		if(event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			if(this.p.getConfig().getInt("award") != 0){
				if(!killer.getName().equalsIgnoreCase(player.getName())){
					if(gh.onDuty(player)){
						GuardOverseer.econ.depositPlayer(killer.getName(), this.p.getConfig().getInt("award"));
						killer.sendMessage(Lang.PREFIX.toString()  + Lang.AWARDED.toString(Arrays.asList("%a%"), Arrays.asList(String.valueOf(this.p.getConfig().getInt("award")))));
					}
				}
			}
		}
	}

	/**
	 * Management of guard PvP and guard PvP notifications
	 */
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();
				if(!this.p.getConfig().getBoolean("guard-pvp")){
					if(gh.onDuty(player) && gh.onDuty(damager)){
						e.setCancelled(true);
						return;
					}
				}
				if(this.p.getConfig().getBoolean("pvp-notifications")){
					if(gh.onDuty(player)){
						if(damager.getItemInHand().getType() == null){
							player.sendMessage(ChatColor.RED + damager.getName() + ChatColor.DARK_BLUE + " has hit you with his hand!");
						}
						player.sendMessage(ChatColor.DARK_RED + damager.getName() + ChatColor.DARK_BLUE + " has hit you with " + ChatColor.DARK_RED + damager.getItemInHand().getType() + ChatColor.DARK_BLUE +  "!");
					}
				}
			}
		}
	}
	
	/**
	 * Prevents players from dropping items
	 */
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e){
		Player p = (Player) e.getPlayer();
		if(gh.onDuty(p)){
			e.setCancelled(true);
		}
	}
	
	/**
	 * Prevents players from typing commands in the config
	 */
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e){
		Player p = (Player) e.getPlayer();
		if(gh.onDuty(p)){
			for(String s:this.p.getConfig().getStringList("disabled-commands")){
				if(e.getMessage().startsWith(s)){
					e.setCancelled(true);
					p.sendMessage(Lang.PREFIX.toString() + Lang.DISABLED_COMMAND.toString());
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
		if(FileManager.getFileManager().getLangConfig().getBoolean("join-message")){
			p.sendMessage(Lang.PREFIX.toString() + ChatColor.BLUE + "Running GuardOverseer, by mydeblob");
		}
		if(p.hasPermission("guardoverseer.update") && GuardOverseer.update && this.p.getConfig().getBoolean("auto-update")){
			p.sendMessage(ChatColor.BLUE + "An update is available: " + GuardOverseer.name + ", a " + GuardOverseer.type + " for " + GuardOverseer.version + "; Available at " + GuardOverseer.link);
			// Will look like - An update is available: AntiCheat v1.5.9, a release for CB 1.6.2-R0.1 available at http://media.curseforge.com/XYZ
			p.sendMessage(ChatColor.BLUE + "Type /gupdate if you would like to automatically download the new jar.");
		}
	}
}
