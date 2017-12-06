package me.nucha.uhcc.commands;

import me.nucha.uhcc.UHCCombat;
import me.nucha.uhcc.language.LanguageManager;
import me.nucha.uhcc.language.Languages;
import me.nucha.uhcc.utils.ConfigUtil;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandUHC implements CommandExecutor {

	public String prefix = "§8[§cUHCCombat§8] §r";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		LanguageManager lm = UHCCombat.languageManager;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("head")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + lm.get("only-players-command"));
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getPlayerHead(sender.getName(), "Free Head");
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + lm.get("gave-own-head"));
				return true;
			}
			if (args[0].equalsIgnoreCase("ghead")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + lm.get("only-players-command"));
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getGoldenHead();
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + lm.get("gave-own-golden-head"));
				return true;
			}
			if (args[0].equalsIgnoreCase("toggle")) {
				if (UHCCombat.UHCModeEnabled) {
					sender.sendMessage(prefix + lm.get("uhc-mode-disabled"));
				} else {
					sender.sendMessage(prefix + lm.get("uhc-mode-enabled"));
				}
				ConfigUtil.setEnableUHCMode(!UHCCombat.UHCModeEnabled);
				return true;
			}
			if (args[0].equalsIgnoreCase("speed")) {
				sender.sendMessage(prefix + lm.get("cmd-usage-speed"));
				return true;
			}
			if (args[0].equalsIgnoreCase("lang")) {
				sender.sendMessage(prefix + lm.get("cmd-usage-lang"));
				return true;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("head")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + lm.get("only-players-command"));
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getPlayerHead(args[1], "Free Head");
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + lm.get("gave-others-head").replaceAll("%player%", args[1]));
				return true;
			}
			if (args[0].equalsIgnoreCase("speed")) {
				if (!StringUtils.isNumeric(args[1])) {
					sender.sendMessage(prefix + lm.get("cmd-input-number"));
					return true;
				}
				int duration = Integer.valueOf(args[1]);
				if (!(duration > 0)) {
					sender.sendMessage(prefix + lm.get("cmd-input-number-greater-than-1"));
					return true;
				}
				int before = UHCCombat.headSpeedDuration;
				ConfigUtil.setHeadDuration(duration);
				sender.sendMessage(prefix
						+ lm.get("changed-head-speed-duration").replaceAll("%before%", String.valueOf(before))
								.replaceAll("%duration%", String.valueOf(duration)));
				return true;
			}
			if (args[0].equalsIgnoreCase("lang")) {
				if (args[1].equalsIgnoreCase("japanese") || args[1].equalsIgnoreCase("english")) {
					Languages lang = Languages.valueOf(args[1].toUpperCase());
					ConfigUtil.setLanguage(lang);
					UHCCombat.languageManager.loadMessages(lang);
					sender.sendMessage(prefix + lm.get("set-lang").replaceAll("%language%", lang.getName()));
				} else {
					sender.sendMessage(prefix + lm.get("cmd-langs"));
				}
				return true;
			}
		}
		sender.sendMessage(prefix + "§a------------ §cUHCCombat §aby §eNucha §a------------");
		sender.sendMessage(prefix + lm.get("cmd-usage-head"));
		sender.sendMessage(prefix + lm.get("cmd-usage-ghead"));
		sender.sendMessage(prefix + lm.get("cmd-usage-toggle"));
		sender.sendMessage(prefix + lm.get("cmd-usage-speed"));
		sender.sendMessage(prefix + lm.get("cmd-usage-lang"));
		return true;
	}

}
