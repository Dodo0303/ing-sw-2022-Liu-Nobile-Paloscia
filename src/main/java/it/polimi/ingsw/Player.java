package it.polimi.ingsw;

public abstract class Player {

    /** A Player in GAME, initially playing COLOR. */
    Player(Color color) {
        _color = color;
        //TODO
    }

    /** Return the color I am currently playing. */
    final Color getColor() {
        return _color;
    }

    /** My current color. */
    private Color _color;

}
