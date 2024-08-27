package net.hetmastertje.scoreboard.boards;

import net.hetmastertje.scoreboard.AbstractScoreboard;
import net.hetmastertje.scoreboard.ScoreboardInterface;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.Language;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.util.Util;
import tk.shanebee.hg.util.Validate;

public class StartingBoard extends AbstractScoreboard {

    private final Main plugin;

    public StartingBoard(Game game, Objective board) {
        super(game, board);
        this.plugin = game.plugin;
    }


    @Override
    public void updateBoard() {
        Language lang = plugin.getLang();
        String alive = "  " + lang.players_alive_num.replace("<num>", String.valueOf(game.getGamePlayerData().getPlayers().size()));

        setTitle(lang.scoreboard_title);
        setLine(15, " ");
        setLine(14, lang.scoreboard_arena);
        setLine(13, "  &e" + game.getGameArenaData().getName());
        setLine(12, " ");
        setLine(11, lang.players_alive);
        setLine(10, alive);
        setLine(9, "Starting");
    }

}
