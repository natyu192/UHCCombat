package me.nucha.uhcc.language;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.nucha.uhcc.UHCCombat;

public class LanguageManager {

	public HashMap<String, String> msgMap;
	public FileConfiguration yml;

	public LanguageManager(Languages lang, UHCCombat plugin) {
		msgMap = new HashMap<>();
		loadMessagesYml(plugin);
		loadMessages(UHCCombat.language);
	}

	public void loadMessagesYml(UHCCombat plugin) {
		File f = new File(plugin.getDataFolder() + "/messages.yml");
		plugin.saveResource("messages.yml", false);
		yml = YamlConfiguration.loadConfiguration(f);
	}

	public void loadMessages(Languages lang) {
		msgMap.clear();
		String name = lang.getName().toUpperCase();
		List<String> paths = new ArrayList<String>();
		paths.add("only-players-command");
		paths.add("gave-own-head");
		paths.add("gave-others-head");
		paths.add("gave-golden-head");
		paths.add("uhc-mode-enabled");
		paths.add("uhc-mode-disabled");
		paths.add("double-hp-enabled");
		paths.add("double-hp-disabled");
		paths.add("death-lightning-enabled");
		paths.add("death-lightning-disabled");
		paths.add("cmd-usage-head");
		paths.add("cmd-usage-ghead");
		paths.add("cmd-usage-toggle");
		paths.add("cmd-usage-speed");
		paths.add("cmd-usage-hp");
		paths.add("cmd-usage-lightning");
		paths.add("cmd-usage-lang");
		paths.add("cmd-input-number");
		paths.add("cmd-input-number-greater-than-1");
		paths.add("cmd-langs");
		paths.add("changed-head-speed-duration");
		paths.add("set-lang");
		for (String path : paths) {
			String p = name + "." + path;
			if (yml.isSet(p)) {
				msgMap.put(path, ChatColor.translateAlternateColorCodes('&', yml.getString(p)));
			} else {
				msgMap.put(path, p);
			}
		}
	}

	public String get(String path) {
		return msgMap.get(path);
	}

}
