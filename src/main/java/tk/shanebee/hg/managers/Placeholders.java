package tk.shanebee.hg.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.Language;
import tk.shanebee.hg.data.Leaderboard;

/**
 * Internal placeholder class
 */
public class Placeholders extends PlaceholderExpansion {

    private final Main plugin;
    private final Leaderboard leaderboard;
    private final Language lang;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
        this.leaderboard = plugin.getLeaderboard();
        this.lang = plugin.getLang();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hungergames";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.startsWith("lb_player_")) {
            int leader = Integer.parseInt(identifier.replace("lb_player_", ""));
            if (leaderboard.getStatsPlayers(Leaderboard.Stats.WINS).size() >= leader)
                return leaderboard.getStatsPlayers(Leaderboard.Stats.WINS).get(leader - 1);
            else
                return lang.lb_blank_space;
        }
        if (identifier.startsWith("lb_score_")) {
            int leader = (Integer.parseInt(identifier.replace("lb_score_", "")));
            if (leaderboard.getStatsScores(Leaderboard.Stats.WINS).size() >= leader)
                return leaderboard.getStatsScores(Leaderboard.Stats.WINS).get(leader - 1);
            else
                return lang.lb_blank_space;

        }
        if (identifier.startsWith("lb_combined_")) {
            int leader = (Integer.parseInt(identifier.replace("lb_combined_", "")));
            if (leaderboard.getStatsPlayers(Leaderboard.Stats.WINS).size() >= leader)
                return leaderboard.getStatsPlayers(Leaderboard.Stats.WINS).get(leader - 1) + lang.lb_combined_separator +
                        leaderboard.getStatsScores(Leaderboard.Stats.WINS).get(leader - 1);
            else
                return lang.lb_blank_space + lang.lb_combined_separator + lang.lb_blank_space;
        }
        if (identifier.equalsIgnoreCase("lb_player")) {
            return String.valueOf(leaderboard.getStat(player.getUniqueId(), Leaderboard.Stats.WINS));
        }
        String[] id = identifier.split("_");
        switch (id[0]) {
            case "lb":
                switch (id[1]) {
                    case "wins":
                    case "kills":
                    case "deaths":
                    case "games":
                        if (id[2].equalsIgnoreCase("p"))
                            return getStatPlayers(identifier);
                        else if (id[2].equalsIgnoreCase("s"))
                            return getStatScores(identifier);
                        else if (id[2].equalsIgnoreCase("c"))
                            return getStatPlayers(identifier) + " : " + getStatScores(identifier);
                        else if (id[2].equalsIgnoreCase("player"))
                            return getStatsPlayer(identifier, player);
                }
            case "status":
                return Main.getPlugin().getManager().getGame(id[1]).getGameArenaData().getStatus().getName();
            case "cost":
                return String.valueOf(Main.getPlugin().getManager().getGame(id[1]).getGameArenaData().getCost());
            case "playerscurrent":
                return String.valueOf(Main.getPlugin().getManager().getGame(id[1]).getGamePlayerData().getPlayers().size());
            case "playersmax":
                return String.valueOf(Main.getPlugin().getManager().getGame(id[1]).getGameArenaData().getMaxPlayers());
            case "playersmin":
                return String.valueOf(Main.getPlugin().getManager().getGame(id[1]).getGameArenaData().getMinPlayers());
        }
        return null;
    }

    private String getStatsPlayer(String identifier, OfflinePlayer player) {
        String[] ind = identifier.split("_");
        Leaderboard.Stats stat = Leaderboard.Stats.valueOf(ind[1].toUpperCase());
        return String.valueOf(leaderboard.getStat(player.getUniqueId(), stat));
    }

    private String getStatPlayers(String identifier) {
        String[] ind = identifier.split("_");
        Leaderboard.Stats stat = Leaderboard.Stats.valueOf(ind[1].toUpperCase());
        int leader = (Integer.parseInt(ind[3]));
        if (leaderboard.getStatsPlayers(stat).size() >= leader) {
            return leaderboard.getStatsPlayers(stat).get(leader - 1);
        } else {
            return lang.lb_blank_space;
        }
    }

    private String getStatScores(String identifier) {
        String[] ind = identifier.split("_");
        Leaderboard.Stats stat = Leaderboard.Stats.valueOf(ind[1].toUpperCase());
        int leader = (Integer.parseInt(ind[3]));
        if (leaderboard.getStatsScores(stat).size() >= leader) {
            return leaderboard.getStatsScores(stat).get(leader - 1);
        } else {
            return lang.lb_blank_space;
        }
    }

}
