package me.nucha.uhcc.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.nucha.uhcc.UHCCombat;

public class UHCListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (UHCCombat.UHCModeEnabled || UHCCombat.doubleHPEnabled) {
			Player p = event.getPlayer();
			p.setMaxHealth(40);
			if (p.getHealth() == 20) {
				p.setHealth(40);
			}
		}
	}

	@EventHandler
	public void onEatHead(PlayerInteractEvent event) {
		if (UHCCombat.UHCModeEnabled && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack item = p.getItemInHand();
			if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
				return;
			}
			String name = item.getItemMeta().getDisplayName();
			if (name.startsWith("§c§l") && name.endsWith("'s Head")) {
				event.setCancelled(true);
				if (item.getAmount() == 1) {
					p.setItemInHand(new ItemStack(Material.AIR));
				} else if (item.getAmount() > 1) {
					ItemStack itemClone = item.clone();
					itemClone.setAmount(item.getAmount() - 1);
					p.setItemInHand(itemClone);
				}
				boolean giveSpeed = true;
				boolean giveRegen = true;
				int speedduration = UHCCombat.headSpeedDuration * 20;
				int regenduration = 7 * 20;
				if (p.getActivePotionEffects() != null) {
					for (PotionEffect pot : p.getActivePotionEffects()) {
						if (giveSpeed && pot.getType().equals(PotionEffectType.SPEED) && pot.getDuration() * 20 >= speedduration) {
							giveSpeed = false;
							continue;
						}
						if (giveRegen && pot.getType().equals(PotionEffectType.REGENERATION) && pot.getDuration() * 20 >= regenduration) {
							giveRegen = false;
							continue;
						}
					}
				}
				if (giveSpeed)
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, UHCCombat.headSpeedDuration * 20, 1));
				if (giveRegen)
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1));
				p.sendMessage("§aYou ate a player head and gained 7 seconds of Regeneration II!");
			}
			if (name.equalsIgnoreCase("§6Golden Head")) {
				event.setCancelled(true);
				if (item.getAmount() == 1) {
					p.setItemInHand(new ItemStack(Material.AIR));
				} else if (item.getAmount() > 1) {
					ItemStack itemClone = item.clone();
					itemClone.setAmount(item.getAmount() - 1);
					p.setItemInHand(itemClone);
				}
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, UHCCombat.headSpeedDuration * 20, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9 * 20, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 2, 0));
				p.sendMessage("§aYou ate a §6Golden Head §aand gained 9 seconds of Regeneration III and 2 minutes of Absorption!");
			}
		}
	}

}
