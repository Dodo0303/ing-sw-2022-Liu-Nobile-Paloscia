package it.polimi.ingsw;

public class Player {
    /** A Player in GAME, initially playing COLOR. */
    Player(Game game, Color color) {
        _game = game;
        _color = color;
    }

    /** Return the color I am currently playing. */
    final Color getColor() {
        return _color;
    }

    /** Return the Game I am currently playing in. */
    final Game getGame() {
        return _game;
    }

    /** My current color. */
    private Color _color;

    /** The game I'm in. */
    private final Game _game;
}

