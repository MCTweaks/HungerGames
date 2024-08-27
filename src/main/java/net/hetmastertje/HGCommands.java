package net.hetmastertje;


import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.commands.*;
import tk.shanebee.hg.data.Config;

import java.util.ArrayList;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;
/**
 * HungerGames SubCommands Register
 */
public class HGCommands {
    final Main plugin;

    public Map<String, BaseCmd> cmds;

    public HGCommands() {
        this.plugin = Main.getPlugin();
    }


    public void register() {
        registerCommand("team", new TeamCmd());
        registerCommand("addspawn", new AddSpawnCmd());
        registerCommand("create", new CreateCmd());
        registerCommand("join", new JoinCmd());
        registerCommand("leave", new LeaveCmd());
        registerCommand("reload", new ReloadCmd());
        registerCommand("setlobbywall", new SetLobbyWallCmd());
        registerCommand("wand", new WandCmd());
        registerCommand("kit", new KitCmd());
        registerCommand("debug", new DebugCmd());
        registerCommand("list", new ListCmd());
        registerCommand("listgames", new ListGamesCmd());
        registerCommand("forcestart", new StartCmd());
        registerCommand("stop", new StopCmd());
        registerCommand("toggle", new ToggleCmd());
        registerCommand("setexit", new SetExitCmd());
        registerCommand("delete", new DeleteCmd());
        registerCommand("chestrefill", new ChestRefillCmd());
        registerCommand("chestrefillnow", new ChestRefillNowCmd());
        registerCommand("bordersize", new BorderSizeCmd());
        registerCommand("bordercenter", new BorderCenterCmd());
        registerCommand("bordertimer", new BorderTimerCmd());
        if (Config.spectateEnabled) {
            registerCommand("spectate", new SpectateCmd());
        }
//        if (nbtApi != null) {
//            registerCommand("nbt", new NBTCmd());
//        }

        ArrayList<String> cArray = new ArrayList<>();
        cArray.add("join");
        cArray.add("leave");
        cArray.add("kit");
        cArray.add("listgames");
        cArray.add("list");

        for (String bc : cmds.keySet()) {
            getServer().getPluginManager().addPermission(new Permission("hg." + bc));
            if (cArray.contains(bc))
                getServer().getPluginManager().getPermission("hg." + bc).setDefault(PermissionDefault.TRUE);

        }
    }

    private void registerCommand(String commandName, BaseCmd executor) {
        PluginCommand cmd = plugin.getCommand(commandName);
//        if (cmd != null) {
//            cmd.setExecutor((CommandExecutor) executor);
//        } else {
//            plugin.getLogger().warning("Failed to register command " + commandName + ". Command not found in plugin.yml.");
//        }

        cmds.put(commandName, executor);
    }

    public Map<String, BaseCmd> getCommands() {
        return cmds;
    }
}
