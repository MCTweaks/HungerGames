package tk.shanebee.hg.game;

import tk.shanebee.hg.Main;
import tk.shanebee.hg.data.Language;

/**
 * General class for storing different aspects of data for {@link Game Games}
 */
public abstract class Data {

    final Game game;
    final Main plugin;
    final Language lang;

    protected Data(Game game) {
        this.game = game;
        this.plugin = game.plugin;
        this.lang = game.lang;
    }

    /**
     * Get the {@link Game} this data belongs to
     *
     * @return Game this data belongs to
     */
    public Game getGame() {
        return game;
    }

    /**
     * Quick method to access the main plugin
     *
     * @return Instance of {@link Main plugin}
     */
    public Main getPlugin() {
        return plugin;
    }

}
