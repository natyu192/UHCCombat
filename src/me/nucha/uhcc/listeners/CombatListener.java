package me.nucha.uhcc.listeners;

import java.math.BigDecimal;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.nucha.uhcc.UHCCombat;
import me.nucha.uhcc.utils.CustomItem;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class CombatListener implements Listener {

	private UHCCombat plugin;

	public CombatListener(UHCCombat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (UHCCombat.UHCModeEnabled || UHCCombat.deathLightningEnabled) {
			Player p = event.getEntity();
			p.getWorld().strikeLightningEffect(p.getLocation());
		}
		if (UHCCombat.UHCModeEnabled) {
			Player p = event.getEntity();
			ItemStack skull = new CustomItem(Material.SKULL_ITEM, 1, "§c§l" + p.getName() + "'s Head", 3);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skullMeta.setOwner(p.getName());
			String newDeathMessage = p.getName() + " was slain!";
			if (p.getKiller() != null) {
				Player k = p.getKiller();
				newDeathMessage = p.getName() + " was slain by " + k.getName();
				String name = (k.getDisplayName() != null ? k.getDisplayName() : k.getName());
				skullMeta.setLore(Arrays.asList(new String[] { "§7Slain by " + name }));
				if (p.getLastDamageCause() == null || p.getLastDamageCause().getCause() == null) {
					ItemStack item = k.getItemInHand();
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
						newDeathMessage += " using [" + item.getItemMeta().getDisplayName() + "]";
					}
				} else {
					switch (p.getLastDamageCause().getCause()) {
					case PROJECTILE:
						ItemStack item = p.getItemInHand();
						newDeathMessage = p.getName() + " was shot by " + k.getName();
						if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
							newDeathMessage += " using [" + item.getItemMeta().getDisplayName() + "]";
						}
						double distance = p.getLocation().distance(k.getLocation());
						BigDecimal distancebi = new BigDecimal(distance);
						double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
						newDeathMessage += " (" + newdistance + " blocks)";
						break;
					case FALL:
						newDeathMessage = p.getName() + " knocked off a cliff by " + k.getName() + "!";
						break;
					case LAVA:
						newDeathMessage = p.getName() + " tried to swim in lava to escape " + k.getName();
						break;
					case FIRE:
						newDeathMessage = p.getName() + " walked into fire whilst fighting " + k.getName();
						break;
					default:
						break;
					}
				}
			} else {
				String messageSuffix = "was slain!";
				switch (p.getLastDamageCause().getCause()) {
				case FALL:
					messageSuffix = "fell to their death!";
					break;
				case SUFFOCATION:
					messageSuffix = "suffocated in a wall!";
					break;
				case LAVA:
					messageSuffix = "tried to swim in lava!";
					break;
				case FIRE:
					messageSuffix = "walked into fire!";
					break;
				case DROWNING:
					messageSuffix = "drowned";
					break;
				case ENTITY_EXPLOSION:
					messageSuffix = "exploded";
					break;
				default:
					break;
				}
				newDeathMessage = p.getName() + " " + messageSuffix;
				skullMeta.setLore(Arrays.asList(new String[] { "§7" + messageSuffix }));
			}
			skull.setItemMeta(skullMeta);
			// event.getDrops().add(skull);
			p.getWorld().dropItemNaturally(p.getLocation(), skull);
			event.setDeathMessage("§c" + newDeathMessage);
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
			}, 20 * 3);
		}
	}

	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if ((UHCCombat.UHCModeEnabled || UHCCombat.doubleHPEnabled) && event.getEntity() instanceof Player
				&& event.getRegainReason() == RegainReason.SATIATED) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onProjectileDamage(EntityDamageByEntityEvent event) {
		if (UHCCombat.UHCModeEnabled) {
			if (event.getDamager() instanceof Projectile && !event.isCancelled()) {
				Projectile pro = (Projectile) event.getDamager();
				if (pro.getShooter() instanceof Player) {
					Player p = (Player) pro.getShooter();
					Player e = (Player) event.getEntity();
					if (pro.getType().equals(EntityType.ARROW)) {
						double health1 = e.getHealth();
						Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
							public void run() {
								double distance = p.getLocation().distance(e.getLocation());
								BigDecimal distancebi = new BigDecimal(distance);
								double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
								if (distance >= 50) {
									Bukkit.broadcastMessage("§f" + p.getName() + " §eshot §f" + e.getName() + " §efrom §c" + newdistance
											+ " §eblocks away!");
								}
								double damage = health1 - e.getHealth();
								BigDecimal bi = new BigDecimal(damage);
								double d = bi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
								double health = e.getHealth();
								BigDecimal di = new BigDecimal(health);
								double h = di.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
								p.sendMessage("§bHit §c" + e.getName() + " §bfor " + d + " §bdamage (" + h + " remaining).");
								p.sendMessage("§c" + e.getDisplayName() + " §eis on §c" + (int) e.getHealth() + " §eHP!");
							}
						}, 1L);
					}
					if (pro.getType().equals(EntityType.FISHING_HOOK)) {
						p.sendMessage("§c" + e.getDisplayName() + " §eis on §c" + (int) e.getHealth() + " §eHP!");
					}
				}
			}
		}
	}

}
