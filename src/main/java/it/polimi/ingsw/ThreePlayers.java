package it.polimi.ingsw;

public class ThreePlayers implements NumOfPlayers {

    public Cloud[] initCloud() {
        Cloud[] clouds = new Cloud[3];
        clouds[0] = new CloudThreePlayers();
        clouds[1] = new CloudThreePlayers();
        clouds[2] = new CloudThreePlayers();
        return clouds;
    }

    /*
    public Player[] initPlayer() {
    }
     */
}
