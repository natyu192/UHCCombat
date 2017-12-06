package me.nucha.uhcc.utils;

import me.nucha.uhcc.UHCCombat;
import me.nucha.uhcc.language.Languages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConfigUtil {

	public static String enable_uhc_mode;
	public static String head_speed_duration;
	public static String language;

	public static void init() {
		enable_uhc_mode = "enable-UHC-mode";
		head_speed_duration = "head-speed-duration";
		language = "language";
	}

	public static void setEnableUHCMode(boolean enabled) {
		UHCCombat.UHCModeEnabled = enabled;
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
	}

	public static void setLanguage(Languages language) {
		UHCCombat.language = language;
	}

}
