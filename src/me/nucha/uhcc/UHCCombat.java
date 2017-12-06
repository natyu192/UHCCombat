package me.nucha.uhcc;

import java.util.Arrays;

import me.nucha.uhcc.commands.CommandUHC;
import me.nucha.uhcc.language.LanguageManager;
import me.nucha.uhcc.language.Languages;
import me.nucha.uhcc.listeners.CombatListener;
import me.nucha.uhcc.listeners.UHCListener;
import me.nucha.uhcc.utils.ConfigUtil;
import me.nucha.uhcc.utils.CustomItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCCombat extends JavaPlugin {

	private static UHCCombat plugin;
	public static boolean UHCModeEnabled;
	public static int headSpeedDuration;
	public static Languages language;
	public static LanguageManager languageManager;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		ConfigUtil.init();
		initValues();
		registerEvents();
		registerCommands();
		registerRecipes();
		languageManager = new LanguageManager(language, this);
	}

	@Override
	public void onDisable() {
		getConfig().set(ConfigUtil.enable_uhc_mode, UHCModeEnabled);
		getConfig().set(ConfigUtil.head_speed_duration, headSpeedDuration);
		getConfig().set(ConfigUtil.language, language.name());
		saveConfig();
	}

	private void initValues() {
		plugin = this;
		if (getConfig().isSet(ConfigUtil.enable_uhc_mode)) {
			UHCModeEnabled = getConfig().getBoolean(ConfigUtil.enable_uhc_mode);
		} else {
			UHCModeEnabled = true;
		}
		if (getConfig().isSet(ConfigUtil.head_speed_duration)) {
			headSpeedDuration = getConfig().getInt(ConfigUtil.head_speed_duration);
		} else {
			headSpeedDuration = 20;
		}
		if (getConfig().isSet(ConfigUtil.language)) {
			language = Languages.valueOf(getConfig().getString(ConfigUtil.language).toUpperCase());
		} else {
			language = Languages.ENGLISH;
		}
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new UHCListener(), this);
		pm.registerEvents(new CombatListener(this), this);
	}

	private void registerCommands() {
		getCommand("uhc").setExecutor(new CommandUHC());
	}

	@SuppressWarnings("deprecation")
	private void registerRecipes() {
		ItemStack ghead = getGoldenHead();
		ShapedRecipe sp = new ShapedRecipe(ghead);
		sp.shape("GGG", "GHG", "GGG");
		sp.setIngredient('G', Material.GOLD_INGOT);
		sp.setIngredient('H', Material.SKULL_ITEM, 3);
		getServer().addRecipe(sp);
	}

	public static UHCCombat getInstance() {
		return plugin;
	}

	public static ItemStack getPlayerHead(String name, String reason) {
		ItemStack skull = new CustomItem(Material.SKULL_ITEM, 1, "§c§l" + name + "'s Head", 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwner(name);
		skullMeta.setLore(Arrays.asList(new String[] { "§7" + reason }));
		skull.setItemMeta(skullMeta);
		return skull;
	}

	public static ItemStack getGoldenHead() {
		ItemStack skull = new CustomItem(Material.SKULL_ITEM, 1, "§6Golden Head", 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setLore(Arrays.asList(new String[] { "§7Absorption (2:00)", "§7Regeneration II (0:05)", "§6*Only you*" }));
		skull.setItemMeta(skullMeta);
		return skull;
	}

}
