package net.hetmastertje.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.util.Util;
import tk.shanebee.hg.util.Validate;

public abstract class AbstractScoreboard implements ScoreboardInterface {
    public final Game game;
    private final Objective board;
    private final org.bukkit.scoreboard.Team[] lines = new org.bukkit.scoreboard.Team[15];
    private final String[] entries = new String[]{"&1&r", "&2&r", "&3&r", "&4&r", "&5&r", "&6&r", "&7&r", "&8&r", "&9&r", "&0&r", "&a&r", "&b&r", "&c&r", "&d&r", "&e&r"};
    private final Main plugin;

    public AbstractScoreboard(Game game, Objective board) {
        this.game = game;
        this.plugin = game.plugin;
        this.board = board;
        initializeLines();
    }

    private void initializeLines() {
        Scoreboard scoreboard = board.getScoreboard();
        for (int i = 0; i < lines.length; i++) {
            // Create and register a new Team for each line
            Team team = scoreboard.getTeam("line" + (i + 1));
            if (team == null) {
                team = scoreboard.registerNewTeam("line" + (i + 1));
            }
            lines[i] = team;
            team.setPrefix("");
            team.setSuffix("");
        }
    }


    /**
     * Set a specific line for this scoreboard
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to set (1 - 15)
     * @param text Text to put in line
     */
    protected void setLine(int line, String text) {
        Validate.isBetween(line, 1, 15);
        Team t = lines[line - 1];
        if (ChatColor.stripColor(text).length() > (128 / 2)) {
            String prefix = Util.getColString(text.substring(0, (128 / 2)));
            t.setPrefix(prefix);
            String lastColor = ChatColor.getLastColors(prefix);
            int splitMax = Math.min(text.length(), 128 - lastColor.length());
            t.setSuffix(Util.getColString(lastColor + text.substring((128 / 2), splitMax)));
        } else {
            t.setPrefix(Util.getColString(text));
            t.setSuffix("");
        }
        board.getScore(Util.getColString(entries[line - 1])).setScore(line);
    }

    /**
     * Set the title of this scoreboard
     *
     * @param title Title to set
     */
    protected void setTitle(String title) {
        board.setDisplayName(Util.getColString(title));
    }
}
