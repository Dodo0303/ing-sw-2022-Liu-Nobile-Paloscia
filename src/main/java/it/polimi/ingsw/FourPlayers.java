package it.polimi.ingsw;

public class FourPlayers implements NumOfPlayers{

    public Cloud[] initCloud() {
        Cloud[] clouds = new Cloud[4];
        clouds[0] = new CloudTwoFourPlayers();
        clouds[1] = new CloudTwoFourPlayers();
        clouds[2] = new CloudTwoFourPlayers();
        clouds[3] = new CloudTwoFourPlayers();
        return clouds;
    }

    /*
    public Player[] initPlayer() {
    }
     */
}
