package me.nucha.uhcc.commands;

import me.nucha.uhcc.UHCCombat;
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
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("head")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + "§cゲーム内でのみ実行できます");
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getPlayerHead(sender.getName(), "Free Head");
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + "§aあなたのHeadを手に入れました");
				return true;
			}
			if (args[0].equalsIgnoreCase("ghead")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + "§cゲーム内でのみ実行できます");
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getGoldenHead();
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + "§6Golden Head§aを手に入れました");
				return true;
			}
			if (args[0].equalsIgnoreCase("toggle")) {
				if (UHCCombat.UHCModeEnabled) {
					sender.sendMessage(prefix + "§cUHC Mode§eをOFFにしました");
				} else {
					sender.sendMessage(prefix + "§cUHC Mode§aをONにしました");
				}
				ConfigUtil.setEnableUHCMode(!UHCCombat.UHCModeEnabled);
				return true;
			}
			if (args[0].equalsIgnoreCase("speed")) {
				sender.sendMessage(prefix + "§a/uhc speed <長さ> §2--- §bHeadを食べたときに付くスピードエフェクトの長さを変更します(単位は秒)");
				return true;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("head")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(prefix + "§cゲーム内でのみ実行できます");
					return true;
				}
				Player p = (Player) sender;
				ItemStack head = UHCCombat.getPlayerHead(args[1], "Free Head");
				p.getInventory().addItem(head);
				sender.sendMessage(prefix + "§a" + args[1] + "のHeadを手に入れました");
				return true;
			}
			if (args[0].equalsIgnoreCase("speed")) {
				if (!StringUtils.isNumeric(args[1])) {
					sender.sendMessage(prefix + "§c長さは秒で指定してください");
					return true;
				}
				int duration = Integer.valueOf(args[1]);
				if (!(duration > 0)) {
					sender.sendMessage(prefix + "§c長さは1秒以上を指定してください");
					return true;
				}
				int before = UHCCombat.headSpeedDuration;
				ConfigUtil.setHeadDuration(duration);
				sender.sendMessage(prefix + "§aHeadを食べたときに付くスピードエフェクトの長さを§9" + before + "§a秒から§b" + duration + "§a秒に変更しました");
				return true;
			}
		}
		sender.sendMessage(prefix + "§a------------ §cUHCCombat §aby §eNucha §a------------");
		sender.sendMessage(prefix + "§a/uhc head [名前] §2--- §bHeadを手に入れます");
		sender.sendMessage(prefix + "§a/uhc ghead §2--- §6Golden Head§bを手に入れます");
		sender.sendMessage(prefix + "§a/uhc toggle §2--- §cUHC Mode§bを切り替えます");
		sender.sendMessage(prefix + "§a/uhc speed <長さ> §2--- Headを食べたときに付くスピードエフェクトの長さを変更します(単位は秒)");
		return true;
	}

}
