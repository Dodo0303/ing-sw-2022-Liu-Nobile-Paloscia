package it.polimi.ingsw.Network.Messages.toClient.JoiningPhase;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.CLI.Phase;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.GUI.Phase_GUI;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;

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
        CLI cliClient = (CLI) client.getClient();
        if (nickname == null) {
            System.out.print("Nickname has been taken.\n");
            cliClient.requireNickname();
        } else {
            cliClient.setNickName(nickname);
            cliClient.setPhase(Phase.ChoosingGameMode);
            cliClient.chooseGameMode();
        }
    }

    public void GUIprocess(ServerHandler client) {
        GUI guiClient = (GUI) client.getClient();
        if (nickname == null) {
            guiClient.requireNickname(true);
        } else {
            guiClient.setNickname(nickname);
            guiClient.setCurrPhase(Phase_GUI.ChoosingGameMode);
            guiClient.chooseGameMode("");
        }
    }
}
