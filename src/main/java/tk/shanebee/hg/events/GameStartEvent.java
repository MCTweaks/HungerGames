package tk.shanebee.hg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tk.shanebee.hg.game.Game;

/**
 * Called when a game starts the pregame stage of a game
 */
@SuppressWarnings("unused")
public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Game game;

    public GameStartEvent(Game game) {
        this.game = game;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the game involved in this event
     *
     * @return The game
     */
    public Game getGame() {
        return this.game;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
