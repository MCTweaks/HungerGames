package tk.shanebee.hg;

import io.papermc.lib.PaperLib;
import net.hetmastertje.HGCommands;
import net.hetmastertje.data.GameManagerConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.hg.commands.*;
import tk.shanebee.hg.data.*;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.listeners.*;
import tk.shanebee.hg.managers.*;
import tk.shanebee.hg.util.NBTApi;
import tk.shanebee.hg.util.NoParty;
import tk.shanebee.hg.util.Party;
import tk.shanebee.hg.util.Util;

import java.util.*;

/**
 * <b>Main class for HungerGames</b>
 */
public class Main extends JavaPlugin {

    private static final Party party = new NoParty();
    //Instances
    private static Main plugin;
    private HGCommands hgCommands;
    //Maps
    private Map<UUID, PlayerSession> playerSession;
    private Map<ItemStack, Integer> itemRarityMap;
    private Map<ItemStack, Integer> itemCostMap;
    private Map<ItemStack, Integer> bonusRarityMap;
    private Map<ItemStack, Integer> bonusCostMap;
    //Lists
    private List<Game> games;
    private GameManagerConfig gameManagerConfig;
    private Config config;
    private Manager manager;
    private PlayerManager playerManager;
    private ArenaConfig arenaconfig;
    private KillManager killManager;
    private RandomItems randomItems;
    private Language lang;
    private KitManager kitManager;
    private ItemStackManager itemStackManager;
    private Leaderboard leaderboard;
    //Mobs
    private MobConfig mobConfig;
    //NMS Nbt
    private NBTApi nbtApi;

    /**
     * Get the instance of this plugin
     *
     * @return This plugin
     */
    public static Main getPlugin() {
        return plugin;
    }

    public static Party getParty() {
        return party;
    }

    @Override
    public void onEnable() {
        if (!Util.isRunningMinecraft(1, 13)) {
            Util.warning("HungerGames does not support your version!");
            Util.warning("Only versions 1.13+ are supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        loadPlugin(true);
    }

    public void loadPlugin(boolean load) {
        long start = System.currentTimeMillis();
        plugin = this;

        if (load) {
            hgCommands = new HGCommands();
            hgCommands.cmds = new HashMap<>();
        }
        games = new ArrayList<>();
        playerSession = new HashMap<>();
        itemCostMap = new HashMap<>();
        itemRarityMap = new HashMap<>();
        bonusCostMap = new HashMap<>();
        bonusRarityMap = new HashMap<>();

        gameManagerConfig = new GameManagerConfig(this);
        config = new Config(this);
        Bukkit.getLogger().info("Loading HungerGames by JT122406");
        PaperLib.suggestPaper(this);

        //NMS Nbt
        if (Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
            nbtApi = new NBTApi();
            Util.log("&7NBTAPI found, NBTAPI hook &aenabled");
        }

        lang = new Language(this);
        kitManager = new KitManager();
        itemStackManager = new ItemStackManager(this);
        mobConfig = new MobConfig(this);
        randomItems = new RandomItems(this);
        playerManager = new PlayerManager();
        arenaconfig = new ArenaConfig(this);
        killManager = new KillManager();
        manager = new Manager(this);
        leaderboard = new Leaderboard(this);

        //PAPI check
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
            Util.log("&7PAPI found, Placeholders have been &aenabled");
        } else {
            Util.log("&7PAPI not found, Placeholders have been &cdisabled");
        }

        getCommand("hg").setExecutor(new CommandListener(this));
        if (load) {
            loadCmds();
        }
        getServer().getPluginManager().registerEvents(new WandListener(this), this);
        getServer().getPluginManager().registerEvents(new CancelListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestDropListener(this), this);

        Util.log("HungerGames has been &aenabled&7 in &b%.2f seconds&7!", (float) (System.currentTimeMillis() - start) / 1000);
    }

    public void reloadPlugin() {
        unloadPlugin(true);
    }

    private void unloadPlugin(boolean reload) {
        stopAll();
        games = null;
        playerSession = null;
        itemRarityMap = null;
        itemCostMap = null;
        plugin = null;
        gameManagerConfig = null;
        config = null;
        nbtApi = null;
        lang = null;
        kitManager = null;
        itemStackManager = null;
        mobConfig = null;
        randomItems = null;
        playerManager = null;
        arenaconfig = null;
        killManager = null;
        manager = null;
        leaderboard = null;
        HandlerList.unregisterAll(this);
        if (reload) {
            loadPlugin(false);
        } else {
            hgCommands = null;
        }
    }

    @Override
    public void onDisable() {
        // I know this seems odd, but this method just
        // nulls everything to prevent memory leaks
        unloadPlugin(false);
        Util.log("HungerGames has been disabled!");
    }

    private void loadCmds() {
        hgCommands.register();
    }

    /**
     * Stop all games
     */
    public void stopAll() {
        ArrayList<UUID> ps = new ArrayList<>();
        for (Game g : games) {
            g.cancelTasks();
            g.getGameBlockData().forceRollback();
            ps.addAll(g.getGamePlayerData().getPlayers());
            ps.addAll(g.getGamePlayerData().getSpectators());
        }
        for (UUID u : ps) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) {
                p.closeInventory();
                if (playerManager.hasPlayerData(u)) {
                    Objects.requireNonNull(playerManager.getPlayerData(u)).getGame().getGamePlayerData().leave(p, false);
                    playerManager.removePlayerData(u);
                }
                if (playerManager.hasSpectatorData(u)) {
                    Objects.requireNonNull(playerManager.getSpectatorData(u)).getGame().getGamePlayerData().leaveSpectate(p);
                    playerManager.removePlayerData(u);
                }
            }
        }
        games.clear();
    }

    /**
     * Get an instance of the RandomItems manager
     *
     * @return RandomItems manager
     */
    public RandomItems getRandomItems() {
        return this.randomItems;
    }

    /**
     * Get an instance of the KillManager
     *
     * @return KillManager
     */
    public KillManager getKillManager() {
        return this.killManager;
    }

    /**
     * Get an instance of the plugins main kit manager
     *
     * @return The kit manager
     */
    public KitManager getKitManager() {
        return this.kitManager;
    }

    /**
     * Get an instance of the ItemStackManager
     *
     * @return ItemStackManager
     */
    public ItemStackManager getItemStackManager() {
        return this.itemStackManager;
    }

    /**
     * Get the instance of the manager
     *
     * @return The manager
     */
    public Manager getManager() {
        return this.manager;
    }

    /**
     * Get an instance of the PlayerManager
     *
     * @return PlayerManager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Get an instance of the ArenaConfig
     *
     * @return ArenaConfig
     */
    public ArenaConfig getArenaConfig() {
        return this.arenaconfig;
    }

    /**
     * Get an instance of HG's leaderboards
     *
     * @return Leaderboard
     */
    public Leaderboard getLeaderboard() {
        return this.leaderboard;
    }

    /**
     * Get a list of all loaded games
     *
     * @return A list of games
     */
    public List<Game> getGames() {
        return this.games;
    }

    /**
     * Get player sessions map
     *
     * @return Player Sessions map
     */
    public Map<UUID, PlayerSession> getPlayerSessions() {
        return this.playerSession;
    }

    /**
     * Get a map of commands
     *
     * @return Map of commands
     */
//    public Map<String, BaseCmd> getCommands() {
//        return this.cmds;
//    }

    public Map<String, BaseCmd> getCommands() {
        return hgCommands.cmds;
    }

    /**
     * Get an instance of the language file
     *
     * @return Language file
     */
    public Language getLang() {
        return this.lang;
    }

    /**
     * Get an instance of {@link Config}
     *
     * @return GameManager Config file
     */
    public GameManagerConfig getGameManagerConfig() {
        return gameManagerConfig;
    }

    /**
     * Get an instance of {@link Config}
     *
     * @return Config file
     */
    public Config getHGConfig() {
        return config;
    }

    /**
     * Get an instance of the mob confile
     *
     * @return Mob config
     */
    public MobConfig getMobConfig() {
        return this.mobConfig;
    }

    /**
     * Get the NBT API
     *
     * @return NBT API
     */
    public NBTApi getNbtApi() {
        return this.nbtApi;
    }

    public Map<ItemStack, Integer> getItemRarityMap() {
        return itemRarityMap;
    }

    public Map<ItemStack, Integer> getItemCostMap() {
        return itemCostMap;
    }

    public Map<ItemStack, Integer> getBonusRarityMap() {
        return bonusRarityMap;
    }

    public Map<ItemStack, Integer> getBonusCostMap() {
        return bonusCostMap;
    }

}
