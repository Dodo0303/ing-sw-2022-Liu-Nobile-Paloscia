package it.polimi.ingsw;

public class TwoPlayers implements NumOfPlayers {

    public Cloud[] initCloud() {
        Cloud[] clouds = new Cloud[2];
        clouds[0] = new CloudTwoFourPlayers();
        clouds[1] = new CloudTwoFourPlayers();
        return clouds;
    }

    /*
    public Player[] initPlayer() {
    }
     */
}
