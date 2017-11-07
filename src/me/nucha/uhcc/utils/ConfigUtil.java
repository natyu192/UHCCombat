package me.nucha.uhcc.utils;

import me.nucha.uhcc.UHCCombat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConfigUtil {

	public static String enable_uhc_mode;
	public static String head_speed_duration;
	private static UHCCombat plugin;

	public static void init(UHCCombat plugin) {
		enable_uhc_mode = "enable-UHC-mode";
		head_speed_duration = "head-speed-duration";
		ConfigUtil.plugin = plugin;
	}

	public static void setEnableUHCMode(boolean enabled) {
		UHCCombat.UHCModeEnabled = enabled;
		plugin.getConfig().set(enable_uhc_mode, enabled);
		plugin.saveConfig();
		if (enabled) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setMaxHealth(40);
				if (p.getHealth() == 20) {
					p.setHealth(40);
				}
			}
		} else {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setMaxHealth(20);
			}
		}
	}

	public static void setHeadDuration(int sec) {
		UHCCombat.headSpeedDuration = sec;
		plugin.getConfig().set(head_speed_duration, sec);
		plugin.saveConfig();
	}

}
