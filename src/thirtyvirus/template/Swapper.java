package thirtyvirus.template;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.template.commands.MainPluginCommand;
import thirtyvirus.template.events.block.BlockClick;
import thirtyvirus.template.events.chat.TabComplete;
import thirtyvirus.template.events.inventory.InventoryClick;
import thirtyvirus.template.helpers.Utilities;

import java.io.File;
import java.util.*;

public class Swapper extends JavaPlugin {

    // console and IO
    private File langFile;
    private FileConfiguration langFileConfig;

    // chat messages
    private Map<String, String> phrases = new HashMap<>();

    // core settings
    public static String prefix = "&c&l[&5&lSwapperPlus&c&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String consolePrefix = "[SwapperPlus] ";

    // customizable settings
    public static String player1 = "";
    public static String player2 = "";
    public static boolean doSwap = false;
    public static int maxSeconds = 120;

    public static int timeTil = 10;

    public void onEnable(){
        // load config.yml (generate one if not there)
        loadConfiguration();

        // load language.yml (generate one if not there)
        loadLangFile();

        // register commands and events
        registerCommands();
        registerEvents();

        // posts confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");

        // schedule swap
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (doSwap) Utilities.doittt();
            }
        }, 20, 20);

    }

    public void onDisable(){
        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    private void registerCommands() {
        getCommand("swapper").setExecutor(new MainPluginCommand(this));

        // set up tab completion
        getCommand("swapper").setTabCompleter(new TabComplete(this));
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockClick(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
    }

    // load the config file and apply settings
    public void loadConfiguration() {
        // prepare config.yml (generate one if not there)
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Utilities.loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));

        player1 = config.getString("player-1");
        player2 = config.getString("player-2");
        maxSeconds = config.getInt("maximum-seconds");

        Bukkit.getLogger().info(consolePrefix + "Settings Reloaded from config");
    }

    // load the language file and apply settings
    public void loadLangFile() {

        // load language.yml (generate one if not there)
        langFile = new File(getDataFolder(), "language.yml");
        langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "language.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String priceString : langFileConfig.getKeys(false)) {
            phrases.put(priceString, langFileConfig.getString(priceString));
        }
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }

}
