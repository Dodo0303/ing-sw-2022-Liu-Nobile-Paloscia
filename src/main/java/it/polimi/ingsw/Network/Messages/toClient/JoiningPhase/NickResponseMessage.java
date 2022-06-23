package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.CLI.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

import java.io.IOException;

/** If the nickname chosen by players is available, then the server should respond with the chosen nickname,
 * otherwise return with null.
 */
public class NickResponseMessage extends MessageToClient {
    private String nickname;
    public NickResponseMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void process(ServerHandler client) throws InterruptedException {
        if (nickname == null) {
            System.out.print("Nickname has been taken.\n");
            client.getClient().requireNickname();
        } else {
            client.setNickName(nickname);
            client.getClient().setPhase(Phase.ChoosingGameMode);
            client.getClient().chooseGameMode();
        }
    }

    public void GUIprocess(it.polimi.ingsw.Client.GUI.ServerHandler client) {
        if (nickname == null) {
            client.getClient().requireNickname(true);
        } else {
            client.getClient().setNickname(nickname);
            client.getClient().setCurrPhase(Phase_GUI.ChoosingGameMode);
            client.getClient().chooseGameMode("");
        }
    }
}
