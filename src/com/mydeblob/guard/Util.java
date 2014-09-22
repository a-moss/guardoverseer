package com.mydeblob.guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Util {
	static Util instance = new Util();
	private GuardOverseer p;
	
	public void init(GuardOverseer p){
		this.p = p;
	}
	
	public static Util getInstance(){
		return instance;
	}
	
	public static Map<String, Enchantment> initEnchantments(){
		Map<String, Enchantment> ENCHANTMENTS = new HashMap<String, Enchantment>();
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
		return ENCHANTMENTS;
	}
	
	public static Map<String, PotionEffectType> initPotions(){
		Map<String, PotionEffectType> POTIONS = new HashMap<String, PotionEffectType>();
		POTIONS.put("speed", PotionEffectType.SPEED);
		POTIONS.put("fastwalking", PotionEffectType.SPEED);
		POTIONS.put("walkfast", PotionEffectType.SPEED);
		POTIONS.put("slow", PotionEffectType.SLOW);
		POTIONS.put("slowness", PotionEffectType.SLOW);
		POTIONS.put("slowwalking", PotionEffectType.SLOW);
		POTIONS.put("walkfast", PotionEffectType.SLOW);
		POTIONS.put("haste", PotionEffectType.FAST_DIGGING);
		POTIONS.put("fastdigging", PotionEffectType.FAST_DIGGING);
		POTIONS.put("fastmining", PotionEffectType.FAST_DIGGING);
		POTIONS.put("digfast", PotionEffectType.FAST_DIGGING);
		POTIONS.put("minefast", PotionEffectType.FAST_DIGGING);
		POTIONS.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
		POTIONS.put("slowdigging", PotionEffectType.SLOW_DIGGING);
		POTIONS.put("slowmining", PotionEffectType.SLOW_DIGGING);
		POTIONS.put("digslow", PotionEffectType.SLOW_DIGGING);
		POTIONS.put("mineslow", PotionEffectType.SLOW_DIGGING);
		POTIONS.put("jump", PotionEffectType.JUMP);
		POTIONS.put("jumpboost", PotionEffectType.JUMP);
		POTIONS.put("boostjump", PotionEffectType.JUMP);
		POTIONS.put("jumphigh", PotionEffectType.JUMP);
		POTIONS.put("highjump", PotionEffectType.JUMP);
		POTIONS.put("confusion", PotionEffectType.CONFUSION);
		POTIONS.put("nausea", PotionEffectType.CONFUSION);
		POTIONS.put("sickness", PotionEffectType.CONFUSION);
		POTIONS.put("dizzy", PotionEffectType.CONFUSION);
		POTIONS.put("regeneration", PotionEffectType.REGENERATION);
		POTIONS.put("regen", PotionEffectType.REGENERATION);
		POTIONS.put("regenerate", PotionEffectType.REGENERATION);
		POTIONS.put("regeneratehealth", PotionEffectType.REGENERATION);
		POTIONS.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
		POTIONS.put("damageresistance", PotionEffectType.DAMAGE_RESISTANCE);
		POTIONS.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
		POTIONS.put("fireimmunity", PotionEffectType.FIRE_RESISTANCE);
		POTIONS.put("fireimmune", PotionEffectType.FIRE_RESISTANCE);
		POTIONS.put("nofire", PotionEffectType.FIRE_RESISTANCE);
		POTIONS.put("waterbreathing", PotionEffectType.WATER_BREATHING);
		POTIONS.put("breathwater", PotionEffectType.WATER_BREATHING);
		POTIONS.put("gills", PotionEffectType.WATER_BREATHING);
		POTIONS.put("invisibility", PotionEffectType.INVISIBILITY);
		POTIONS.put("invisible", PotionEffectType.INVISIBILITY);
		POTIONS.put("notseen", PotionEffectType.INVISIBILITY);
		POTIONS.put("ghost", PotionEffectType.INVISIBILITY);
		POTIONS.put("nightvision", PotionEffectType.NIGHT_VISION);
		POTIONS.put("seeindark", PotionEffectType.NIGHT_VISION);
		POTIONS.put("nightgoggles", PotionEffectType.NIGHT_VISION);
		POTIONS.put("weakness", PotionEffectType.WEAKNESS);
		POTIONS.put("notstrong", PotionEffectType.WEAKNESS);
		POTIONS.put("weak", PotionEffectType.WEAKNESS);
		POTIONS.put("strength", PotionEffectType.INCREASE_DAMAGE);
		POTIONS.put("increasedamage", PotionEffectType.INCREASE_DAMAGE);
		POTIONS.put("increaseddamage", PotionEffectType.INCREASE_DAMAGE);
		POTIONS.put("strong", PotionEffectType.INCREASE_DAMAGE);
		return POTIONS;
	}
	
	/**
	 * Gives the player the potions (Must be exact names; Use Arrays.asList)
	 * 
	 * @param p - The player that should recieve the potions
	 * @param potions - The potions that the player should recieve (Names must be exact)
	 */
	public void givePotions(Player p, ArrayList<String> potions){
		for(String s:potions){
			if(this.p.POTIONS.containsKey(s)){
				p.addPotionEffect(new PotionEffect(this.p.POTIONS.get(s), Integer.MAX_VALUE))
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
	
	/**
	 * Removes all active potions on the player
	 * 
	 * @param p - The player to remove the potion effects from
	 */
	public void removeAllPotions(Player p){
		for(PotionEffect pe:p.getActivePotionEffects()){
			p.removePotionEffect(pe.getType());
		}
	}
	
	/**
	 * Gives the player the permissions listed (Use Arrays.asList)
	 * 
	 * @param p - The player to give the permissions
	 * @param permissions - The permissions to give
	 */
	public void givePermissions(Player p, ArrayList<String> permissions){
		if(GuardOverseer.vaultEnabled){
			for(String s:permissions){
				GuardOverseer.perms.playerAdd(p, s);
			}
		}
	}
	
	/**
	 * Remove the player the permissions listed (Use Arrays.asList)
	 * 
	 * @param p - The player to give the permissions
	 * @param permissions - The permissions to remove
	 */
	public void removePermissions(Player p, ArrayList<String> permissions){
		if(GuardOverseer.vaultEnabled){
			for(String s:permissions){
				GuardOverseer.perms.playerRemove(p, s);
			}
		}
	}
	
	/**
	 * Clears everything in the players inventory (Including armor), remove the players experience
	 * and sets the players gamemode to survival
	 * 
	 * @param p - The player to perform the actions on
	 */
	public void clearPlayer(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setHelmet(new ItemStack(Material.AIR));
		pi.setLeggings(new ItemStack(Material.AIR));
		pi.setChestplate(new ItemStack(Material.AIR));
		pi.setBoots(new ItemStack(Material.AIR));
		p.setExp(0.0F);
		p.setGameMode(GameMode.SURVIVAL);
	}

	/**
	 * Returns the players gamemode (Name of it) as a string
	 * 
	 * @param p - The player to get the gamemode
	 * @return String - The name of the gamemode
	 */
	public String getGamemode(Player p){
		if(p.getGameMode().equals(GameMode.ADVENTURE)){
			return "Adventure";
		}else if(p.getGameMode().equals(GameMode.CREATIVE)){
			return "Creative";
		}else{
			return "Survival";
		}
	}
}
