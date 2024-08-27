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
//        initializeLines();
    }

//    private void initializeLines() {
//        Scoreboard scoreboard = board.getScoreboard();
//        for (int i = 0; i < lines.length; i++) {
//            // Create and register a new Team for each line
//            Team team = scoreboard.getTeam("line" + (i + 1));
//            if (team == null) {
//                team = scoreboard.registerNewTeam("line" + (i + 1));
//            }
//            lines[i] = team;
//            team.setPrefix("");
//            team.setSuffix("");
//        }
//    }


    /**
     * Set a specific line for this scoreboard
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to set (1 - 15)
     * @param text Text to put in line
     */
    public void setScoreOrLine(int line, Object content) {
        Validate.isBetween(line, 1, 15);

        // Retrieve the objective and scoreboard
        Objective objective = board;
        Scoreboard scoreboard = objective.getScoreboard();

        String teamName = "line" + line;
        Team team = scoreboard.getTeam(teamName); // Check if the team already exists
        String entry = "§".repeat(line);

        // Handle null content
        if (content == null) {
            if (team != null) {
                team.unregister();
                lines[line - 1] = null;
            }
            scoreboard.resetScores(entry);
            return;
        }

        // Register new team if it doesn't exist
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.addEntry(entry);
            lines[line - 1] = team;
        }

        // Handle content as a Component or String
        if (content instanceof net.kyori.adventure.text.Component) {
            team.prefix((net.kyori.adventure.text.Component) content);
        } else if (content instanceof String) {
            String text = (String) content;
            if (ChatColor.stripColor(text).length() > (128 / 2)) {
                String prefix = Util.getColString(text.substring(0, (128 / 2)));
                team.setPrefix(prefix);
                String lastColor = ChatColor.getLastColors(prefix);
                int splitMax = Math.min(text.length(), 128 - lastColor.length());
                team.setSuffix(Util.getColString(lastColor + text.substring((128 / 2), splitMax)));
            } else {
                team.setPrefix(Util.getColString(text));
                team.setSuffix("");
            }
        }

        objective.getScore(entry).setScore(line);
    }



    /**
     * Set the title of this scoreboard
     *
     * @param title Title to set
     */
    protected void setTitle(String title) {
        board.setDisplayName(Util.getColString(title));
    }

    protected void removeAllLines() {
        // Retrieve the objective and scoreboard
        Objective objective = board;
        Scoreboard scoreboard = objective.getScoreboard();

        // Loop through possible lines and remove them
        for (int line = 1; line <= 15; line++) {
            String teamName = "line" + line;
            Team team = scoreboard.getTeam(teamName);

            if (team != null) {
                team.unregister();  // Remove the team
            }

            String entry = "§".repeat(line);
            scoreboard.resetScores(entry);  // Remove the score associated with the line
        }
    }
}
