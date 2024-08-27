package net.hetmastertje.scoreboard.boards;

import net.hetmastertje.scoreboard.AbstractScoreboard;
import org.bukkit.scoreboard.Objective;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.Language;
import tk.shanebee.hg.game.Game;

public class RunningBoard extends AbstractScoreboard {

    private final Main plugin;

    public RunningBoard(Game game, Objective board) {
        super(game, board);
        this.plugin = game.plugin;
    }

    @Override
    public void updateBoard() {
        Language lang = plugin.getLang();
        String alive = "  " + lang.players_alive_num.replace("<num>", String.valueOf(game.getGamePlayerData().getPlayers().size()));

        setTitle(lang.scoreboard_title);
        setScoreOrLine(15, " ");
        setScoreOrLine(14, lang.scoreboard_arena);
        setScoreOrLine(13, "  &e" + game.getGameArenaData().getName());
        setScoreOrLine(12, " ");
        setScoreOrLine(11, lang.players_alive);
        setScoreOrLine(10, alive);
        setScoreOrLine(9, "Running");
    }
}
