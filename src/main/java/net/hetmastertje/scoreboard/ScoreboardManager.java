package net.hetmastertje.scoreboard;

import net.hetmastertje.scoreboard.boards.CountdownBoard;
import net.hetmastertje.scoreboard.boards.DefaultBoard;
import net.hetmastertje.scoreboard.boards.RunningBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.Config;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.util.Util;

import java.util.*;

/**
 * Manages the scoreboard for the game, handling teams and updating the scoreboard based on game status.
 */
public class ScoreboardManager {

    private static final ChatColor[] COLORS;

    static {
        COLORS = new ChatColor[]{ChatColor.AQUA, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.GOLD};
    }

    private final Game game;
    private final Main plugin;
    private final Scoreboard scoreboard;
    private final Objective board;
    private final Team team;

    public static final ArrayList<UUID> participants = new ArrayList<>();
    public ScoreboardManager(Game game) {
        this.game = game;
        this.plugin = game.plugin;
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        board = scoreboard.registerNewObjective("Board", "dummy", "Board");
        board.setDisplaySlot(DisplaySlot.SIDEBAR);
        board.setDisplayName(" ");

        // Register the game-team with a non-numeric name
        team = scoreboard.registerNewTeam("game-team");

        if (Config.hideNametags) {
            team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
        }

        startScoreboard();
    }

    public Team registerTeam(String name) {
        Team team = scoreboard.registerNewTeam(name);
        String prefix = Util.getColString(plugin.getLang().team_prefix.replace("<name>", name) + " ");
        team.setPrefix(prefix);
        String suffix = Util.getColString(" " + plugin.getLang().team_suffix.replace("<name>", name));
        team.setSuffix(suffix);
        team.setColor(COLORS[game.gamePlayerData.teams.size() % COLORS.length]);
        if (Config.hideNametags && Config.team_showTeamNames) {
            team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.FOR_OTHER_TEAMS);
        }
        team.setAllowFriendlyFire(Config.team_friendly_fire);
        team.setCanSeeFriendlyInvisibles(Config.team_see_invis);
        return team;
    }

    /**
     * Adds a player to this scoreboard.
     *
     * @param player Player to add
     */
    public void setBoard(Player player) {
        player.setScoreboard(scoreboard);
        team.addEntry(player.getName());
        addPlayer(player);
    }


    /**
     * Updates the scoreboard based on the current game status.
     */
    public void updateBoard() {
        ScoreboardInterface scoreboard;
//        System.out.println(game.getGameArenaData().getStatus());

        switch (game.getGameArenaData().getStatus()) {
            case RUNNING:
                scoreboard = new RunningBoard(game, board);
                break;
            case COUNTDOWN:
                scoreboard = new CountdownBoard(game, board);
                break;
            // Add other cases for different statuses
            default:
                scoreboard = new DefaultBoard(game, board);
                break;
        }

        scoreboard.updateBoard();

    }

    @Override
    public String toString() {
        return "Board{game=" + game + '}';
    }

    private void startScoreboard() {
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), () -> {
            participants.forEach(uuid -> game.gameArenaData.updateBoards());
        }, 20L, 20L);
    }


    public static void removePlayer(Player player) {
        participants.remove(player.getUniqueId());
    }

    public static void addPlayer(Player player) {
        if (!participants.contains(player.getUniqueId())) {
            participants.add(player.getUniqueId());
        }
    }
}
