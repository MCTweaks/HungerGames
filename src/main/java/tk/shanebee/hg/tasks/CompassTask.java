package tk.shanebee.hg.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.PlayerData;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.managers.PlayerManager;

import java.util.UUID;

public class CompassTask implements Runnable {

    private final PlayerManager playerManager;

    public CompassTask(Main plugin) {
        this.playerManager = plugin.getPlayerManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this, 25L, 25L);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {

            if (p.getInventory().contains(Material.COMPASS)) {
                PlayerData pd = playerManager.getPlayerData(p.getUniqueId());

                if (pd != null) {

                    String[] st = getNearestPlayer(p, pd);
                    String info = ChatColor.translateAlternateColorCodes('&',
                            Main.getPlugin().getLang().compass_nearest_player.replace("<player>", st[0]).replace("<distance>", st[1]));

                    for (ItemStack it : p.getInventory()) {
                        if (it != null && it.getType() == Material.COMPASS) {
                            ItemMeta im = it.getItemMeta();
                            assert im != null;
                            im.setDisplayName(info);
                            it.setItemMeta(im);
                        }
                    }
                }

            }
        }
    }

    private int cal(int i) {
        if (i < 0) {
            return -i;
        }
        return i;
    }

    private String[] getNearestPlayer(Player p, PlayerData pd) {

        Game g = pd.getGame();

        int i = 200000;

        Player player = null;

        for (UUID u : g.getGamePlayerData().getPlayers()) {

            Player p2 = Bukkit.getPlayer(u);

            if (p2 != null && !p2.equals(p) && !pd.isOnTeam(u)) {

                Location l = p2.getLocation();

                int c = cal((int) (p.getLocation().getBlockX() - l.getX())) + cal((int) (p.getLocation().getBlockY() - l.getY())) + cal((int) (p.getLocation().getBlockZ() - l.getZ()));

                if (i > c) {
                    player = p2;
                    i = c;
                }
            }
        }
        if (player != null) p.setCompassTarget(player.getLocation());

        return new String[]{(player == null ? "none" : player.getName()), String.valueOf(i)};
    }
}
